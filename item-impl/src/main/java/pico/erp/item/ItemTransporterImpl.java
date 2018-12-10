package pico.erp.item;

import com.coreoz.windmill.Windmill;
import com.coreoz.windmill.exports.config.ExportHeaderMapping;
import com.coreoz.windmill.exports.exporters.excel.ExportExcelConfig;
import com.coreoz.windmill.files.FileSource;
import com.coreoz.windmill.imports.Parsers;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.attachment.AttachmentId;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.item.category.ItemCategoryId;
import pico.erp.item.category.ItemCategoryMapper;
import pico.erp.item.spec.type.ItemSpecTypeId;
import pico.erp.shared.LabeledValue;
import pico.erp.shared.Public;
import pico.erp.shared.data.ContentInputStream;
import pico.erp.shared.data.LocalizedNameable;
import pico.erp.shared.data.UnitKind;
import pico.erp.shared.event.EventPublisher;

@Component
@Public
@Validated
@Transactional
public class ItemTransporterImpl implements ItemTransporter {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemCategoryMapper itemCategoryMapper;

  @Autowired
  private MessageSource messageSource;

  @Lazy
  @Autowired
  private CompanyService companyService;

  @Autowired
  private EventPublisher eventPublisher;

  @Override
  @SneakyThrows
  public ContentInputStream exportExcel(ExportRequest request) {
    val locale = LocaleContextHolder.getLocale();
    Stream<Item> items = request.isEmpty() ? Stream.empty() : itemRepository.getAll();
    val workbook = new XSSFWorkbook();
    val bytes = Windmill
      .export(() -> items.iterator())
      .withHeaderMapping(
        new ExportHeaderMapping<Item>()
          .add("id", e -> e.getId().getValue())
          .add("code", e -> e.getCode().getValue())
          .add("externalCode", e -> e.getExternalCode())
          .add("categoryId",
            e -> e.getCategory() != null ? e.getCategory().getId().getValue() : null)
          .add("name", e -> e.getName())
          .add("unit", e -> to(e.getUnit(), locale))
          .add("baseUnitCost", e -> e.getBaseUnitCost())
          .add("type", e -> to(e.getType(), locale))
          .add("status", e -> to(e.getStatus(), locale))
          .add("specTypeId",
            e -> e.getSpecTypeId() != null ? e.getSpecTypeId().getValue() : null)
          .add("description", e -> e.getDescription())
          .add("customerId",
            e -> e.getCustomer() != null ? e.getCustomer().getId().getValue() : null)
          .add("purchasable", e -> e.isPurchasable() + "")
          .add("attachmentId",
            e -> e.getAttachmentId() != null ? e.getAttachmentId().getValue() : null)
      )
      .asExcel(
        ExportExcelConfig.fromWorkbook(workbook).build("items")
      )
      .toByteArray();
    return ContentInputStream.builder()
      .name(
        String.format("items-%s.%s",
          DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(OffsetDateTime.now()),
          ContentInputStream.XLSX_CONTENT_EXTENSION
        )
      )
      .contentType(ContentInputStream.XLSX_CONTENT_TYPE)
      .contentLength(bytes.length)
      .inputStream(new ByteArrayInputStream(bytes))
      .build();
  }

  @Override
  public void importExcel(ImportRequest request) {

    val items = Parsers.xlsx("items")
      .trimValues()
      .parse(FileSource.of(request.getInputStream()))
      .skip(1)
      .map(row -> Item.builder()
        .id(ItemId.from(row.cell("id").asString()))
        .code(ItemCode.from(row.cell("code").asString()))
        .externalCode(row.cell("externalCode").asString())
        .category(
          Optional.ofNullable(row.cell("categoryId").asString())
            .map(id -> ItemCategoryId.from(id))
            .map(id -> itemCategoryMapper.map(id))
            .orElse(null)
        )
        .name(row.cell("name").asString())
        .unit(
          Optional.ofNullable(row.cell("unit").asString())
            .map(value -> new LabeledValue(value))
            .map(value -> value.getValue())
            .map(value -> UnitKind.valueOf(value))
            .orElse(null)
        )
        .baseUnitCost(new BigDecimal(row.cell("baseUnitCost").asString()))
        .type(
          Optional.ofNullable(row.cell("type").asString())
            .map(value -> new LabeledValue(value))
            .map(value -> value.getValue())
            .map(value -> ItemTypeKind.valueOf(value))
            .orElse(null)
        )
        .status(
          Optional.ofNullable(row.cell("status").asString())
            .map(value -> new LabeledValue(value))
            .map(value -> value.getValue())
            .map(value -> ItemStatusKind.valueOf(value))
            .orElse(null)
        )
        .specTypeId(
          Optional.ofNullable(row.cell("specTypeId").asString())
            .map(id -> ItemSpecTypeId.from(id))
            .orElse(null)
        )
        .description(row.cell("description").asString())
        .customer(
          Optional.ofNullable(row.cell("customerId").asString())
            .map(id -> CompanyId.from(id))
            .map(id -> companyService.get(id))
            .orElse(null)
        )
        .purchasable(Boolean.valueOf(row.cell("purchasable").asString()))
        .attachmentId(
          Optional.ofNullable(row.cell("attachmentId").asString())
            .map(id -> AttachmentId.from(id))
            .orElse(null)
        )
        .build()
      );

    items.forEach(item -> {
      val previous = itemRepository.findBy(item.getId()).orElse(null);
      val response = item.apply(new ItemMessages.PrepareImportRequest(previous));
      if (previous == null) {
        itemRepository.create(item);
      } else if (request.isOverwrite()) {
        itemRepository.update(item);
      }
      eventPublisher.publishEvents(response.getEvents());
    });

  }

  private LabeledValue to(LocalizedNameable nameable, Locale locale) {
    if (nameable == null) {
      return null;
    }
    return new LabeledValue(nameable.name(),
      messageSource.getMessage(nameable.getNameCode(), null, locale));
  }


}
