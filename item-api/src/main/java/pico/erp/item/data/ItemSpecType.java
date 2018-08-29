package pico.erp.item.data;


import java.io.Serializable;
import java.math.BigDecimal;

public interface ItemSpecType<T extends ItemSpecVariables> {

  BigDecimal calculateUnitCost(ItemInfo item, T variables);

  T create();

  String getDescription();

  ItemSpecTypeId getId();

  Serializable getMetadata();

  String getName();

  Class<T> getType();

}
