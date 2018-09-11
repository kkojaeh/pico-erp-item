package pico.erp.item.core;

import pico.erp.item.category.ItemCategory;
import pico.erp.item.data.ItemCategoryCode;
import pico.erp.item.data.ItemCode;
import pico.erp.item.data.ItemLotCode;
import pico.erp.item.domain.Item;
import pico.erp.item.lot.ItemLot;

public interface ItemCodeGenerator {

  ItemCategoryCode generate(ItemCategory itemCategory);

  ItemCode generate(Item item);

  ItemLotCode generate(ItemLot item);
}
