package pico.erp.item.lot;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.shared.data.Auditor;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemLotData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ItemLotId id;

  ItemId itemId;

  ItemSpecCode specCode;

  ItemLotCode lotCode;

  OffsetDateTime expirationDate;

  boolean expired;

  OffsetDateTime expiredDate;

  Auditor createdBy;

  OffsetDateTime createdDate;

}
