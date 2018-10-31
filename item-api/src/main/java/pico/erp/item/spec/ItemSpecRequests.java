package pico.erp.item.spec;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;

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

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class LockRequest {

    @Valid
    @NotNull
    ItemSpecId id;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class UnlockRequest {

    @Valid
    @NotNull
    ItemSpecId id;

  }
}
