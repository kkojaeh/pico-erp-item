package pico.erp.item.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.Optional;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.company.CompanyService;
import pico.erp.company.data.CompanyData;
import pico.erp.company.data.CompanyId;
import pico.erp.item.ItemExceptions.ItemNotFoundException;
import pico.erp.item.ItemSpecTypeExceptions.NotFoundException;
import pico.erp.item.core.ItemCategoryRepository;
import pico.erp.item.core.ItemRepository;
import pico.erp.item.core.ItemSpecTypeRepository;
import pico.erp.item.data.ItemCategoryId;
import pico.erp.item.data.ItemId;
import pico.erp.item.data.ItemSpecType;
import pico.erp.item.data.ItemSpecTypeId;
import pico.erp.item.data.ItemSpecVariables;
import pico.erp.item.domain.Item;
import pico.erp.item.domain.ItemCategory;
import pico.erp.item.domain.ItemLot;
import pico.erp.item.domain.ItemSpec;
import pico.erp.item.impl.jpa.ItemCategoryEntity;
import pico.erp.item.impl.jpa.ItemEntity;
import pico.erp.item.impl.jpa.ItemLotEntity;
import pico.erp.item.impl.jpa.ItemSpecEntity;

@Mapper
public abstract class ItemJpaMapper {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Lazy
  @Autowired
  protected ItemSpecTypeRepository itemSpecTypeRepository;

  @Lazy
  @Autowired
  protected ItemRepository itemRepository;

  @Lazy
  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  protected ItemCategory map(ItemCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> itemCategoryRepository.findBy(id)
        .orElseThrow(pico.erp.item.ItemCategoryExceptions.NotFoundException::new))
      .orElse(null);
  }

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  protected ItemSpecType map(ItemSpecTypeId specTypeId) {
    return Optional.ofNullable(specTypeId)
      .map(id -> itemSpecTypeRepository.findBy(id)
        .orElseThrow(NotFoundException::new)
      )
      .orElse(null);
  }

  protected Item map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(id -> itemRepository.findBy(id)
        .orElseThrow(ItemNotFoundException::new)
      )
      .orElse(null);
  }

  @SneakyThrows
  protected String map(ItemSpecVariables variables) {
    if (variables == null) {
      return null;
    }
    return objectMapper.writeValueAsString(variables);
  }

  public ItemCategory map(ItemCategoryEntity entity) {
    if (entity == null) {
      return null;
    }
    return ItemCategory.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .name(entity.getName())
      .parent(map(entity.getParentId()))
      .path(entity.getPath())
      .key(entity.getKey())
      .itemCount(entity.getItemCount())
      .description(entity.getDescription())
      .build();
  }

  @Mappings({
    @Mapping(target = "parentId", source = "parent.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemCategoryEntity map(ItemCategory itemCategory);

  public Item map(ItemEntity entity) {
    return Item.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .name(entity.getName())
      .externalCode(entity.getExternalCode())
      .category(map(entity.getCategory()))
      .unit(entity.getUnit())
      .baseUnitCost(entity.getBaseUnitCost())
      .type(entity.getType())
      .description(entity.getDescription())
      .status(entity.getStatus())
      .customerData(map(entity.getCustomerId()))
      .specType(map(entity.getSpecTypeId()))
      .sellable(entity.isSellable())
      .purchasable(entity.isPurchasable())
      .attachmentId(entity.getAttachmentId())
      .build();
  }

  @Mappings({
    @Mapping(target = "category", source = "category.id"),
    @Mapping(target = "customerId", source = "customerData.id"),
    @Mapping(target = "customerName", source = "customerData.name"),
    @Mapping(target = "specTypeId", source = "specType.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemEntity map(Item item);

  @SuppressWarnings("unchecked")
  public ItemSpec map(ItemSpecEntity entity) {
    Item item = map(entity.getItem());
    ItemSpecType itemSpecType = item.getSpecType();
    return ItemSpec.builder()
      .id(entity.getId())
      .summary(entity.getSummary())
      .item(item)
      .variables(mapAs(entity.getVariables(), itemSpecType.getType()))
      .baseUnitCost(entity.getBaseUnitCost())
      .build();
  }

  @Mappings({
    @Mapping(target = "item", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemSpecEntity map(ItemSpec spec);

  @Mappings({
    @Mapping(target = "item", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemLotEntity map(ItemLot lot);

  public ItemLot map(ItemLotEntity entity) {
    return ItemLot.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .item(map(entity.getItem()))
      .expirationDate(entity.getExpirationDate())
      .expired(entity.isExpired())
      .expiredDate(entity.getExpiredDate())
      .build();
  }

  @SneakyThrows
  protected ItemSpecVariables mapAs(String variables, Class<ItemSpecVariables> type) {
    if (variables == null) {
      return null;
    }
    return objectMapper.readValue(variables, type);
  }

  public abstract void pass(ItemCategoryEntity from, @MappingTarget ItemCategoryEntity to);

  public abstract void pass(ItemEntity from, @MappingTarget ItemEntity to);

  public abstract void pass(ItemSpecEntity from, @MappingTarget ItemSpecEntity to);

  public abstract void pass(ItemLotEntity from, @MappingTarget ItemLotEntity to);

}
