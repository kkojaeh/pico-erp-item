package pico.erp.item.lot;

import java.time.LocalDateTime;
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

  LocalDateTime expirationDate;

  boolean expired;

  LocalDateTime expiredDate;

  Auditor createdBy;

  LocalDateTime createdDate;

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
