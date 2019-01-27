package pico.erp.item;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.company.CompanyData;
import pico.erp.company.CompanyId;
import pico.erp.company.CompanyService;
import pico.erp.item.ItemExceptions.NotFoundException;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.category.ItemCategoryId;
import pico.erp.item.category.ItemCategoryMapper;
import pico.erp.item.spec.type.ItemSpecType;
import pico.erp.item.spec.type.ItemSpecTypeId;
import pico.erp.item.spec.type.ItemSpecTypeMapper;

@Mapper
public abstract class ItemMapper {

  @Lazy
  @Autowired
  protected ItemCategoryMapper itemCategoryMapper;

  @Lazy
  @Autowired
  protected ItemSpecTypeMapper itemSpecTypeMapper;

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Lazy
  @Autowired
  protected ItemCodeGenerator itemCodeGenerator;

  @Lazy
  @Autowired
  protected ItemRepository itemRepository;

  @Autowired
  protected ItemEntityRepository itemEntityRepository;

  public Item jpa(ItemEntity entity) {
    return Item.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .name(entity.getName())
      .externalCode(entity.getExternalCode())
      .category(map(entity.getCategoryId()))
      .unit(entity.getUnit())
      .baseUnitCost(entity.getBaseUnitCost())
      .type(entity.getType())
      .description(entity.getDescription())
      .status(entity.getStatus())
      .customer(map(entity.getCustomerId()))
      .specTypeId(entity.getSpecTypeId())
      .purchasable(entity.isPurchasable())
      .barcodeNumber(entity.getBarcodeNumber())
      .build();
  }

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "customerId", source = "customer.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemEntity jpa(Item item);

  protected CompanyData map(CompanyId companyId) {
    return Optional.ofNullable(companyId)
      .map(companyService::get)
      .orElse(null);
  }

  /*public ItemEntity jpa(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(itemEntityRepository::findOne)
      .orElse(null);
  }*/

  protected ItemCategory map(ItemCategoryId categoryId) {
    return itemCategoryMapper.map(categoryId);
  }

  public Item map(ItemId itemId) {
    return Optional.ofNullable(itemId)
      .map(id -> itemRepository.findBy(id)
        .orElseThrow(NotFoundException::new)
      )
      .orElse(null);
  }

  @Mappings({
    @Mapping(target = "category", source = "categoryId"),
    @Mapping(target = "customer", source = "customerId"),
    @Mapping(target = "codeGenerator", expression = "java(itemCodeGenerator)"),
  })
  public abstract ItemMessages.CreateRequest map(ItemRequests.CreateRequest request);

  public abstract ItemMessages.DeactivateRequest map(ItemRequests.DeactivateRequest request);

  public abstract ItemMessages.DeleteRequest map(ItemRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "category", source = "categoryId"),
    @Mapping(target = "customer", source = "customerId")
  })
  public abstract ItemMessages.UpdateRequest map(ItemRequests.UpdateRequest request);

  public abstract ItemMessages.ActivateRequest map(ItemRequests.ActivateRequest request);

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "customerId", source = "customer.id")
  })
  public abstract ItemData map(Item item);

  protected ItemSpecType map(ItemSpecTypeId itemSpecTypeId) {
    return itemSpecTypeMapper.map(itemSpecTypeId);
  }

  public abstract void pass(ItemEntity from, @MappingTarget ItemEntity to);

}
