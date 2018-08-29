package pico.erp.item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.data.ItemId;
import pico.erp.item.data.ItemSpecId;
import pico.erp.item.data.ItemSpecVariables;

public interface ItemSpecRequests {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class CreateRequest {

    @Valid
    @NotNull
    ItemSpecId id;

    @Valid
    @NotNull
    ItemId itemId;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UpdateRequest {

    @Valid
    @NotNull
    ItemSpecId id;

    @NotNull
    ItemSpecVariables variables;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    ItemSpecId id;

  }
}
