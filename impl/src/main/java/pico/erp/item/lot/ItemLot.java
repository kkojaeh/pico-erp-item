package pico.erp.item.lot;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.item.Item;
import pico.erp.item.lot.ItemLotEvents.CreatedEvent;
import pico.erp.item.lot.ItemLotEvents.ExpiredEvent;
import pico.erp.item.lot.ItemLotEvents.UpdatedEvent;
import pico.erp.item.lot.ItemLotExceptions.CannotExpireException;
import pico.erp.item.lot.ItemLotMessages.CreateResponse;
import pico.erp.item.lot.ItemLotMessages.DeleteResponse;
import pico.erp.item.lot.ItemLotMessages.ExpireResponse;
import pico.erp.item.lot.ItemLotMessages.UpdateResponse;
import pico.erp.item.spec.ItemSpecCode;
import pico.erp.shared.data.Auditor;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ItemLotId id;

  Item item;

  ItemSpecCode specCode;

  ItemLotCode lotCode;

  OffsetDateTime expirationDate;

  boolean expired;

  OffsetDateTime expiredDate;

  Auditor createdBy;

  OffsetDateTime createdDate;

  public ItemLot() {
    expired = false;
  }

  public CreateResponse apply(ItemLotMessages.CreateRequest request) {
    id = request.getId();
    specCode = request.getSpecCode();
    lotCode = request.getLotCode();
    item = request.getItem();
    expirationDate = request.getExpirationDate();
    return new CreateResponse(
      Arrays.asList(new CreatedEvent(this.id))
    );
  }

  public UpdateResponse apply(ItemLotMessages.UpdateRequest request) {
    expirationDate = request.getExpirationDate();
    return new UpdateResponse(
      Arrays.asList(new UpdatedEvent(this.id))
    );
  }

  public ExpireResponse apply(ItemLotMessages.ExpireRequest request) {
    if (expired) {
      throw new CannotExpireException();
    }
    expired = true;
    expiredDate = OffsetDateTime.now();
    return new ExpireResponse(
      Arrays.asList(new ExpiredEvent(this.id))
    );
  }

  public DeleteResponse apply(ItemLotMessages.DeleteRequest request) {

    if (expired) {
      throw new CannotExpireException();
    }
    expired = true;
    expiredDate = OffsetDateTime.now();
    return new DeleteResponse(
      Arrays.asList(new ExpiredEvent(this.id))
    );
  }

  public ItemLotKey getKey() {
    return ItemLotKey.from(item.getId(), specCode, lotCode);
  }


}
