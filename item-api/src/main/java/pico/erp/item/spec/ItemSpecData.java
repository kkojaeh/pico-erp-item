package pico.erp.item.spec;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pico.erp.item.ItemId;

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

}
