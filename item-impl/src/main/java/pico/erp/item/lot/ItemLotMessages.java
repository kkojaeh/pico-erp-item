package pico.erp.item.lot;

import java.time.OffsetDateTime;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.item.Item;
import pico.erp.item.code.ItemCodeGenerator;
import pico.erp.shared.event.Event;

public interface ItemLotMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    ItemLotId id;

    @NotNull
    @Valid
    ItemLotCode code;

    @Future
    OffsetDateTime expirationDate;

    @NotNull
    Item item;

    @NotNull
    ItemCodeGenerator itemCodeGenerator;

  }

  @Data
  class UpdateRequest {

    @Future
    OffsetDateTime expirationDate;

  }

  @Data
  class ExpireRequest {

  }

  @Data
  class DeleteRequest {

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

  }

  @Value
  class ExpireResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }
}
