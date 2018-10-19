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
import pico.erp.item.ItemEntity;
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
  public ItemSpec domain(ItemSpecEntity entity) {
    Item item = itemMapper.domain(entity.getItem());
    ItemSpecType itemSpecType = item.getSpecType();
    return ItemSpec.builder()
      .id(entity.getId())
      .summary(entity.getSummary())
      .item(item)
      .variables(map(entity.getVariables(), itemSpecType.getType()))
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
  public abstract ItemSpecEntity entity(ItemSpec spec);

  protected ItemEntity entity(ItemId itemId) {
    return itemMapper.entity(itemId);
  }

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