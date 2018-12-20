package pico.erp.item.category;

import com.coreoz.windmill.Windmill;
import com.coreoz.windmill.exports.config.ExportHeaderMapping;
import com.coreoz.windmill.exports.exporters.excel.ExportExcelConfig;
import com.coreoz.windmill.files.FileSource;
import com.coreoz.windmill.imports.Parsers;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.Public;
import pico.erp.shared.data.ContentInputStream;

@Component
@Public
@Validated
@Transactional
public class ItemCategoryTransporterImpl implements ItemCategoryTransporter {

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Autowired
  private ItemCategoryMapper mapper;

  @Override
  @SneakyThrows
  public ContentInputStream exportExcel(ExportRequest request) {

    val context = request.isEmpty() ? new EmptyExportContext() : getExportContext();
    val workbook = new XSSFWorkbook();
    val bytes = Windmill
      .export(context.getCategories())
      .withHeaderMapping(
        new ExportHeaderMapping<ItemCategory>()
          .add("id", e -> e.getId().getValue())
          .add("code", e -> e.getCode().getValue())
          .add("name", e -> this.whiteSpaces(context.getLevel(e.getId()) * 2) + e.getName())
          .add("parentId", e -> e.getParent() != null ? e.getParent().getId().getValue() : null)
          .add("itemCount", e -> e.getItemCount())
          .add("description", e -> e.getDescription())
      )
      .asExcel(
        ExportExcelConfig.fromWorkbook(workbook).build("item-categories")
      )
      .toByteArray();

    return ContentInputStream.builder()
      .name(
        String.format("item-category-%s.%s",
          DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(OffsetDateTime.now()),
          "xlsx"
        )
      )
      .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
      .contentLength(bytes.length)
      .inputStream(new ByteArrayInputStream(bytes))
      .build();
  }

  private Stream<ItemCategory> getAll(ItemCategory itemCategory, int level,
    Map<ItemCategoryId, Integer> levels) {
    levels.put(itemCategory.getId(), level);
    return Stream.concat(
      Stream.of(itemCategory),
      itemCategoryRepository.findChildrenBy(itemCategory.getId())
        .flatMap(child -> this.getAll(child, level + 1, levels))
    );
  }

  private ExportContext getExportContext() {
    val levels = new HashMap<ItemCategoryId, Integer>();
    val categories = itemCategoryRepository.findChildrenBy(null)
      .flatMap(category -> this.getAll(category, 0, levels))
      .collect(Collectors.toList());
    return new ExportContext(categories, levels);
  }

  @Override
  public void importExcel(ImportRequest request) {

    val categories = Parsers.xlsx("item-categories")
      .trimValues()
      .parse(FileSource.of(request.getInputStream()))
      .skip(1)
      .map(row -> ItemCategory.builder()
        .id(ItemCategoryId.from(row.cell("id").asString()))
        .name(row.cell("name").asString())
        .code(ItemCategoryCode.from(row.cell("code").asString()))
        .parent(
          Optional.ofNullable(row.cell("parentId").asString())
            .map(id -> ItemCategoryId.from(id))
            .map(id -> mapper.map(id))
            .orElse(null)
        )
        .itemCount(new BigDecimal(row.cell("itemCount").asInteger().safeValue()))
        .description(row.cell("description").asString())
        .build()
      );

    categories.forEach(category -> {
      val previous = itemCategoryRepository.findBy(category.getId()).orElse(null);
      category.apply(new ItemCategoryMessages.PrepareImportRequest(previous));
      if (previous == null) {
        itemCategoryRepository.create(category);
      } else if (request.isOverwrite()) {
        itemCategoryRepository.update(category);
      }
    });

  }

  private String whiteSpaces(int length) {
    val buffer = new StringBuffer();
    IntStream.range(0, length)
      .forEach(i -> buffer.append("  "));
    return buffer.toString();
  }

  @AllArgsConstructor
  @Getter
  private class ExportContext {

    List<ItemCategory> categories;

    Map<ItemCategoryId, Integer> levels;

    Integer getLevel(ItemCategoryId id) {
      return levels.get(id);
    }

  }

  private class EmptyExportContext extends ExportContext {

    public EmptyExportContext() {
      super(Collections.emptyList(), Collections.emptyMap());
    }

  }

}
