package pico.erp.item.code;

import pico.erp.item.Item;
import pico.erp.item.ItemCode;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.category.ItemCategoryCode;
import pico.erp.item.lot.ItemLot;
import pico.erp.item.lot.ItemLotCode;

public interface ItemCodeGenerator {

  ItemCategoryCode generate(ItemCategory itemCategory);

  ItemCode generate(Item item);

  ItemLotCode generate(ItemLot item);
}
