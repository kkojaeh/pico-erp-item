package pico.erp.item.spec;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;
import pico.erp.item.lot.ItemLotCode;
import pico.erp.item.spec.variables.ItemSpecVariables;
import pico.erp.shared.data.UnitKind;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class ItemSpecData {

  ItemSpecId id;

  ItemId itemId;

  String summary;

  ItemSpecVariables variables;

  BigDecimal baseUnitCost;

  BigDecimal purchaseUnitCost;

  boolean locked;

  UnitKind unit;

  UnitKind purchaseUnit;

  ItemLotCode lotCode;

}
