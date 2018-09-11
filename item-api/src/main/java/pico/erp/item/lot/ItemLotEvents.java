package pico.erp.item.lot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.data.ItemLotId;
import pico.erp.shared.event.Event;

public interface ItemLotEvents {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreatedEvent implements Event {

    public final static String CHANNEL = "event.item-lot.created";

    private ItemLotId itemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeletedEvent implements Event {

    public final static String CHANNEL = "event.item-lot.deleted";

    private ItemLotId itemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ExpiredEvent implements Event {

    public final static String CHANNEL = "event.item-lot.expired";

    private ItemLotId itemLotId;

    public String channel() {
      return CHANNEL;
    }

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdatedEvent implements Event {

    public final static String CHANNEL = "event.item-lot.updated";

    private ItemLotId itemLotId;

    public String channel() {
      return CHANNEL;
    }

  }
}
