package pico.erp.item.lot;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pico.erp.item.ItemId;
import pico.erp.item.spec.ItemSpecCode;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class ItemLotKey implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  @NotNull
  private ItemId itemId;

  @Valid
  @NotNull
  private ItemSpecCode specCode;

  @Valid
  @NotNull
  private ItemLotCode lotCode;

  public static ItemLotKey from(ItemId itemId, ItemSpecCode specCode, ItemLotCode lotCode) {
    return new ItemLotKey(itemId, specCode, lotCode);
  }

  public static ItemLotKey from(ItemId itemId, ItemLotCode lotCode) {
    return new ItemLotKey(itemId, ItemSpecCode.NOT_APPLICABLE, lotCode);
  }

}
