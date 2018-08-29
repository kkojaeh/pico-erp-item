package pico.erp.item.data;

import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemLotData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ItemLotId id;

  ItemId itemId;

  ItemLotCode code;

  OffsetDateTime expirationDate;

  boolean expired;

  OffsetDateTime expiredDate;

}
