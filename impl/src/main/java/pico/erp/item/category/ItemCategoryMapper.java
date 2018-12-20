package pico.erp.item.category;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.item.Item;
import pico.erp.item.ItemCodeGenerator;
import pico.erp.item.ItemId;
import pico.erp.item.ItemMapper;

@Mapper
public abstract class ItemCategoryMapper {

  @Lazy
  @Autowired
  protected ItemMapper itemMapper;

  @Lazy
  @Autowired
  protected ItemCodeGenerator itemCodeGenerator;

  @Lazy
  @Autowired
  protected ItemCategoryCodeGenerator itemCategoryCodeGenerator;

  @Lazy
  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Lazy
  @Autowired
  private ItemCategoryEntityRepository itemCategoryEntityRepository;

  public ItemCategory jpa(ItemCategoryEntity entity) {
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
  public abstract ItemCategoryEntity jpa(ItemCategory itemCategory);

  /*public ItemCategoryEntity jpa(ItemCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(itemCategoryEntityRepository::findOne)
      .orElse(null);
  }*/

  @Mappings({
    @Mapping(target = "parent", source = "parentId"),
    @Mapping(target = "codeGenerator", expression = "java(itemCategoryCodeGenerator)")
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

  public ItemCategory map(ItemCategoryId categoryId) {
    return Optional.ofNullable(categoryId)
      .map(id -> itemCategoryRepository.findBy(id)
        .orElseThrow(ItemCategoryExceptions.NotFoundException::new))
      .orElse(null);
  }

  protected Item map(ItemId itemId) {
    return itemMapper.map(itemId);
  }

  public abstract void pass(ItemCategoryEntity from, @MappingTarget ItemCategoryEntity to);

}
