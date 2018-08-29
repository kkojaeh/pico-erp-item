package pico.erp.item.domain;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import pico.erp.audit.annotation.Audit;
import pico.erp.item.ItemLotEvents.CreatedEvent;
import pico.erp.item.ItemLotEvents.ExpiredEvent;
import pico.erp.item.ItemLotEvents.UpdatedEvent;
import pico.erp.item.ItemLotExceptions.CannotExpireException;
import pico.erp.item.data.ItemLotCode;
import pico.erp.item.data.ItemLotId;
import pico.erp.item.domain.ItemLotMessages.CreateResponse;
import pico.erp.item.domain.ItemLotMessages.DeleteResponse;
import pico.erp.item.domain.ItemLotMessages.ExpireResponse;
import pico.erp.item.domain.ItemLotMessages.UpdateResponse;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Audit(alias = "item-lot")
public class ItemLot implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ItemLotId id;

  ItemLotCode code;

  Item item;

  OffsetDateTime expirationDate;

  boolean expired;

  OffsetDateTime expiredDate;


  public ItemLot() {
    expired = false;
  }

  public CreateResponse apply(ItemLotMessages.CreateRequest request) {
    id = request.getId();
    code = Optional.ofNullable(request.getCode())
      .orElseGet(() -> request.getItemCodeGenerator().generate(this));
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


}
