package pico.erp.item.spec;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.item.Item;
import pico.erp.shared.event.Event;

public interface ItemSpecMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    ItemSpecId id;

    @Valid
    @NotNull
    Item item;

  }

  @Data
  class UpdateRequest {

    @NotNull
    ItemSpecVariables variables;

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
  class DeleteResponse {

    Collection<Event> events;

  }
}
