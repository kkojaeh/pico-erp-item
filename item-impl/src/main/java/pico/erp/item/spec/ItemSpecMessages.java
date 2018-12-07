package pico.erp.item.spec;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import pico.erp.item.Item;
import pico.erp.item.spec.variables.ItemSpecVariables;
import pico.erp.item.spec.variables.ItemSpecVariablesLifecycler;
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

    @NotNull
    ItemSpecVariablesLifecycler itemSpecVariablesLifecycler;

  }

  @Data
  class UpdateRequest {

    @NotNull
    ItemSpecVariables variables;

  }

  @Data
  class LockRequest {

  }

  @Data
  class UnlockRequest {

  }

  @Data
  class DeleteRequest {

  }

  @Data
  class RecalculateRequest {

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

  @Value
  class LockResponse {

    Collection<Event> events;

  }

  @Value
  class UnlockResponse {

    Collection<Event> events;

  }

  @Value
  class RecalculateResponse {

    Collection<Event> events;

  }
}
