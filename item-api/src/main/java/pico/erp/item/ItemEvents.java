package pico.erp.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.category.ItemCategoryId;
import pico.erp.shared.event.Event;

public interface ItemEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.item.created";

    private ItemId itemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.item.deleted";

    private ItemId itemId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CategoryChangedEvent implements Event {

    public final static String CHANNEL = "event.item.category-changed";

    private ItemId itemId;

    private ItemCategoryId oldCategoryId;

    private ItemCategoryId newCategoryId;

    public String channel() {
      return CHANNEL;
    }
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.item.updated";

    private ItemId itemId;

    private boolean nameChanged = false;

    public String channel() {
      return CHANNEL;
    }

  }
}
