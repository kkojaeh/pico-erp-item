package pico.erp.item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.Menu;
import pico.erp.shared.data.MenuCategory;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MENU implements Menu {

  ITEM_CATEGORY_MANAGEMENT("/item-category", "fas fa-tags", MenuCategory.ITEM),
  ITEM_MANAGEMENT("/item", "fas fa-tags", MenuCategory.ITEM);

  String url;

  String icon;

  MenuCategory category;

  public String getId() {
    return name();
  }

}
