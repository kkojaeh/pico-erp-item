package pico.erp.item.code;

import pico.erp.item.Item;
import pico.erp.item.ItemCode;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.category.ItemCategoryCode;

public interface ItemCodeGenerator {

  ItemCategoryCode generate(ItemCategory itemCategory);

  ItemCode generate(Item item);

}
