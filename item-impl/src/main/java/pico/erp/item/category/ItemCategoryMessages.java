package pico.erp.item.category;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import pico.erp.item.code.ItemCodeGenerator;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.event.Event;

public interface ItemCategoryMessages {

  @Data
  class CreateRequest {

    @Valid
    @NotNull
    ItemCategoryId id;

    @Valid
    ItemCategory parent;

    @Pattern(regexp = TypeDefinitions.PATH_NAME_REGEXP)
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @NotNull
    ItemCodeGenerator itemCodeGenerator;

  }

  @Data
  class UpdateRequest {

    @Valid
    ItemCategory parent;

    @Pattern(regexp = TypeDefinitions.PATH_NAME_REGEXP)
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @NotNull
    ItemCodeGenerator itemCodeGenerator;

  }

  @Data
  class DeleteRequest {

  }

  @Data
  @AllArgsConstructor
  class SetParentRequest {

    @Valid
    ItemCategory parent;

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
  class SetParentResponse {

    Collection<Event> events;

  }
}
