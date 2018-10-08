package pico.erp.item.lot;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;

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
    ItemLotCode code;

    @Valid
    @NotNull
    ItemId itemId;

    @Future
    OffsetDateTime expirationDate;


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
    @Past
    OffsetDateTime fixedDate;

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
    OffsetDateTime expirationDate;

  }
}
