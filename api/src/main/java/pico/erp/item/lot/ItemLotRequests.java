package pico.erp.item.lot;

import java.time.LocalDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;

public interface ItemLotRequests {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    ItemLotId id;

    @Valid
    @NotNull
    ItemSpecCode specCode;

    @Valid
    @NotNull
    ItemLotCode lotCode;

    @Valid
    @NotNull
    ItemId itemId;

    @Future
    LocalDateTime expirationDate;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    ItemLotId id;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ExpireRequest {

    /**
     * 지정 기준시간보다 예전 데이터를 삭제
     */
    @NotNull
    LocalDateTime fixedDate;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    ItemLotId id;

    @Future
    LocalDateTime expirationDate;

  }

}
