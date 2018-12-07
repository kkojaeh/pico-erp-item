package pico.erp.item.spec.type;


import java.io.Serializable;
import pico.erp.item.spec.variables.ItemSpecVariables;

public interface ItemSpecType<T extends ItemSpecVariables> {

  T create();

  String getDescription();

  ItemSpecTypeId getId();

  Serializable getMetadata();

  String getName();

  Class<T> getType();

}
