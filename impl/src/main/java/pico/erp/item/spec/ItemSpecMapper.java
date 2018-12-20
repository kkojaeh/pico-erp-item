package pico.erp.item.spec;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.item.Item;
import pico.erp.item.ItemId;
import pico.erp.item.ItemMapper;
import pico.erp.item.spec.variables.ItemSpecVariablesLifecycler;

@Mapper
public abstract class ItemSpecMapper {

  @Lazy
  @Autowired
  protected ItemMapper itemMapper;

  @Autowired
  protected ItemSpecVariablesLifecycler itemSpecVariablesLifecycler;

  @AfterMapping
  protected void afterMapping(ItemSpec domain, @MappingTarget ItemSpecEntity entity) {
    entity.setVariables(
      itemSpecVariablesLifecycler.stringify(domain.getItem().getSpecTypeId(), domain.getVariables())
    );
  }

  @Mappings({
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "variables", ignore = true),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemSpecEntity jpa(ItemSpec spec);

  @SuppressWarnings("unchecked")
  public ItemSpec jpa(ItemSpecEntity entity) {
    Item item = map(entity.getItemId());
    return ItemSpec.builder()
      .id(entity.getId())
      .summary(entity.getSummary())
      .item(item)
      .variables(itemSpecVariablesLifecycler.parse(item.getSpecTypeId(), entity.getVariables()))
      .baseUnitCost(entity.getBaseUnitCost())
      .locked(entity.isLocked())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemId", source = "item.id")
  })
  public abstract ItemSpecData map(ItemSpec itemSpec);

  public abstract ItemSpecMessages.UpdateRequest map(ItemSpecRequests.UpdateRequest request);

  public abstract ItemSpecMessages.DeleteRequest map(ItemSpecRequests.DeleteRequest request);

  public abstract ItemSpecMessages.LockRequest map(ItemSpecRequests.LockRequest request);

  public abstract ItemSpecMessages.UnlockRequest map(ItemSpecRequests.UnlockRequest request);

  protected Item map(ItemId itemId) {
    return itemMapper.map(itemId);
  }

  @Mappings({
    @Mapping(target = "item", source = "itemId"),
    @Mapping(target = "itemSpecVariablesLifecycler", expression = "java(itemSpecVariablesLifecycler)")
  })
  public abstract ItemSpecMessages.CreateRequest map(ItemSpecRequests.CreateRequest request);

  public abstract void pass(ItemSpecEntity from, @MappingTarget ItemSpecEntity to);

}
