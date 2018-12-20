package pico.erp.item.spec.variables;

import pico.erp.item.spec.type.ItemSpecTypeId;

public interface ItemSpecVariablesLifecycler {

  ItemSpecVariables initialize(ItemSpecTypeId typeId);

  ItemSpecVariables parse(ItemSpecTypeId typeId, String text);

  String stringify(ItemSpecTypeId typeId, ItemSpecVariables variables);

}
