package pico.erp.item.lot;

import java.time.OffsetDateTime;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.shared.data.Auditor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemLotView {

  ItemLotId id;

  ItemId itemId;

  ItemSpecCode specCode;

  ItemLotCode lotCode;

  OffsetDateTime expirationDate;

  boolean expired;

  OffsetDateTime expiredDate;

  Auditor createdBy;

  OffsetDateTime createdDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    @Valid
    @NonNull
    ItemId itemId;

    String code;

    Boolean expired;
  }

}
