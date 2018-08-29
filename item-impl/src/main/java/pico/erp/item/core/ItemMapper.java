package pico.erp.item.core;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.company.CompanyService;
import pico.erp.company.data.CompanyData;
import pico.erp.company.data.CompanyId;
import pico.erp.item.ItemCategoryRequests;
import pico.erp.item.ItemExceptions.ItemNotFoundException;
import pico.erp.item.ItemLotRequests;
import pico.erp.item.ItemRequests;
import pico.erp.item.ItemSpecRequests;
import pico.erp.item.ItemSpecTypeExceptions.NotFoundException;
import pico.erp.item.data.ItemCategoryData;
import pico.erp.item.data.ItemCategoryId;
import pico.erp.item.data.ItemData;
import pico.erp.item.data.ItemId;
import pico.erp.item.data.ItemLotData;
import pico.erp.item.data.ItemSpecData;
import pico.erp.item.data.ItemSpecType;
import pico.erp.item.data.ItemSpecTypeData;
import pico.erp.item.data.ItemSpecTypeId;
import pico.erp.item.domain.Item;
import pico.erp.item.domain.ItemCategory;
import pico.erp.item.domain.ItemCategoryMessages;
import pico.erp.item.domain.ItemLot;
import pico.erp.item.domain.ItemLotMessages;
import pico.erp.item.domain.ItemLotMessages.ExpireRequest;
import pico.erp.item.domain.ItemMessages.ActivateRequest;
import pico.erp.item.domain.ItemMessages.CreateRequest;
import pico.erp.item.domain.ItemMessages.DeactivateRequest;
import pico.erp.item.domain.ItemMessages.DeleteRequest;
import pico.erp.item.domain.ItemMessages.UpdateRequest;
import pico.erp.item.domain.ItemSpec;
import pico.erp.item.domain.ItemSpecMessages;

@Mapper
public abstract class ItemMapper {

  @Lazy
  @Autowired
  protected CompanyService companyService;

  @Autowired
  protected ItemSpecTypeRepository itemSpecTypeRepository;

  @Autowired
  protected ItemCodeGenerator itemCodeGenerator;

  @Autowired
  protected ItemRepository itemRepository;

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

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

  @Mappings({
    @Mapping(target = "category", source = "categoryId"),
    @Mapping(target = "customerData", source = "customerId"),
    @Mapping(target = "specType", source = "specTypeId"),
    @Mapping(target = "itemCodeGenerator", expression = "java(itemCodeGenerator)"),
  })
  public abstract CreateRequest map(ItemRequests.CreateRequest request);

  public abstract DeactivateRequest map(ItemRequests.DeactivateRequest request);

  public abstract DeleteRequest map(ItemRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "category", source = "categoryId"),
    @Mapping(target = "customerData", source = "customerId"),
    @Mapping(target = "specType", source = "specTypeId"),
    @Mapping(target = "itemCodeGenerator", expression = "java(itemCodeGenerator)")
  })
  public abstract UpdateRequest map(ItemRequests.UpdateRequest request);

  public abstract ActivateRequest map(ItemRequests.ActivateRequest request);

  @Mappings({
    @Mapping(target = "categoryId", source = "category.id"),
    @Mapping(target = "specTypeId", source = "specType.id"),
    @Mapping(target = "customerId", source = "customerData.id")
  })
  public abstract ItemData map(Item item);

  @Mappings({
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract ItemSpecData map(ItemSpec itemSpec);

  @Mappings({
    @Mapping(target = "item", source = "itemId")
  })
  public abstract ItemSpecMessages.CreateRequest map(ItemSpecRequests.CreateRequest request);

  public abstract ItemSpecMessages.UpdateRequest map(ItemSpecRequests.UpdateRequest request);

  public abstract ItemSpecMessages.DeleteRequest map(ItemSpecRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "parent", source = "parentId"),
    @Mapping(target = "itemCodeGenerator", expression = "java(itemCodeGenerator)")
  })
  public abstract ItemCategoryMessages.CreateRequest map(
    ItemCategoryRequests.CreateRequest request);

  @Mappings({
    @Mapping(target = "parent", source = "parentId"),
    @Mapping(target = "itemCodeGenerator", expression = "java(itemCodeGenerator)")
  })
  public abstract ItemCategoryMessages.UpdateRequest map(
    ItemCategoryRequests.UpdateRequest request);

  public abstract ItemCategoryMessages.DeleteRequest map(
    ItemCategoryRequests.DeleteRequest request);

  @Mappings({
    @Mapping(target = "parentId", source = "parent.id")
  })
  public abstract ItemCategoryData map(ItemCategory itemCategory);

  public abstract ItemSpecTypeData map(ItemSpecType type);


  @Mappings({
    @Mapping(target = "itemId", source = "item.id"),
  })
  public abstract ItemLotData map(ItemLot lot);

  @Mappings({
    @Mapping(target = "item", source = "itemId"),
    @Mapping(target = "itemCodeGenerator", expression = "java(itemCodeGenerator)")
  })
  public abstract ItemLotMessages.CreateRequest map(ItemLotRequests.CreateRequest request);

  public abstract ItemLotMessages.UpdateRequest map(ItemLotRequests.UpdateRequest request);

  public abstract ExpireRequest map(ItemLotRequests.ExpireRequest request);

  public abstract ItemLotMessages.DeleteRequest map(ItemLotRequests.DeleteRequest request);

}
