package pico.erp.item.lot;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import pico.erp.item.Item;
import pico.erp.item.ItemId;
import pico.erp.item.ItemMapper;
import pico.erp.item.code.ItemCodeGenerator;

@Mapper
public abstract class ItemLotMapper {

  @Lazy
  @Autowired
  protected ItemMapper itemMapper;

  @Autowired
  protected ItemCodeGenerator itemCodeGenerator;

  public ItemLot domain(ItemLotEntity entity) {
    return ItemLot.builder()
      .id(entity.getId())
      .code(entity.getCode())
      .item(map(entity.getItemId()))
      .expirationDate(entity.getExpirationDate())
      .expired(entity.isExpired())
      .expiredDate(entity.getExpiredDate())
      .build();
  }

  @Mappings({
    @Mapping(target = "itemId", source = "item.id"),
    @Mapping(target = "createdBy", ignore = true),
    @Mapping(target = "createdDate", ignore = true),
    @Mapping(target = "lastModifiedBy", ignore = true),
    @Mapping(target = "lastModifiedDate", ignore = true)
  })
  public abstract ItemLotEntity entity(ItemLot lot);

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

  public abstract ItemLotMessages.ExpireRequest map(ItemLotRequests.ExpireRequest request);

  public abstract ItemLotMessages.DeleteRequest map(ItemLotRequests.DeleteRequest request);

  protected Item map(ItemId itemId) {
    return itemMapper.map(itemId);
  }

  public abstract void pass(ItemLotEntity from, @MappingTarget ItemLotEntity to);

}
