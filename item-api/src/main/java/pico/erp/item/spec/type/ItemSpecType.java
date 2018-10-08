package pico.erp.item.spec.type;


import java.io.Serializable;
import java.math.BigDecimal;
import pico.erp.item.ItemInfo;
import pico.erp.item.spec.ItemSpecVariables;

public interface ItemSpecType<T extends ItemSpecVariables> {

  BigDecimal calculateUnitCost(ItemInfo item, T variables);

  T create();

  String getDescription();

  ItemSpecTypeId getId();

  Serializable getMetadata();

  String getName();

  Class<T> getType();

}
