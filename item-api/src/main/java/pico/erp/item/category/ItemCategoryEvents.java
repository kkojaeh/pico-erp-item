package pico.erp.item.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.data.ItemCategoryId;
import pico.erp.shared.event.Event;

public interface ItemCategoryEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.item-category.created";

    private ItemCategoryId itemCategoryId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.item-category.deleted";

    private ItemCategoryId itemCategoryId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ParentChangedEvent implements Event {

    public final static String CHANNEL = "event.item-category.parent-changed";

    private ItemCategoryId itemCategoryId;

    private ItemCategoryId oldParentId;

    private ItemCategoryId newParentId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.item-category.updated";

    private ItemCategoryId itemCategoryId;

    public String channel() {
      return CHANNEL;
    }

  }
}
