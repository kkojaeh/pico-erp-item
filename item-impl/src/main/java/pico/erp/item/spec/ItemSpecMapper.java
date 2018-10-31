package pico.erp.item.spec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.item.Item;
import pico.erp.item.ItemId;
import pico.erp.item.ItemMapper;
import pico.erp.item.spec.type.ItemSpecType;

@Mapper
public abstract class ItemSpecMapper {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Lazy
  @Autowired
  protected ItemMapper itemMapper;

  {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @SuppressWarnings("unchecked")
  public ItemSpec jpa(ItemSpecEntity entity) {
    Item item = map(entity.getItemId());
    ItemSpecType itemSpecType = item.getSpecType();
    return ItemSpec.builder()
      .id(entity.getId())
      .summary(entity.getSummary())
      .item(item)
      .variables(map(entity.getVariables(), itemSpecType.getType()))
      .baseUnitCost(entity.getBaseUnitCost())
      .locked(entity.isLocked())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemSpecEntity jpa(ItemSpec spec);

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

  public abstract ItemSpecMessages.LockRequest map(ItemSpecRequests.LockRequest request);

  public abstract ItemSpecMessages.UnlockRequest map(ItemSpecRequests.UnlockRequest request);

  protected Item map(ItemId itemId) {
    return itemMapper.map(itemId);
  }

  @SneakyThrows
  protected ItemSpecVariables map(String variables, Class<ItemSpecVariables> type) {
    if (variables == null) {
      return null;
    }
    return objectMapper.readValue(variables, type);
  }

  @SneakyThrows
  protected String map(ItemSpecVariables variables) {
    if (variables == null) {
      return null;
    }
    return objectMapper.writeValueAsString(variables);
  }

  public abstract void pass(ItemSpecEntity from, @MappingTarget ItemSpecEntity to);

}
