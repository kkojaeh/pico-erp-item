package pico.erp.item.spec.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import pico.erp.item.ItemInfo;
import pico.erp.shared.data.UnitKind;

@JsonTypeInfo(use = Id.CLASS, property = "@type")
public interface ItemSpecVariables extends Serializable {

  BigDecimal calculatePurchaseQuantity(BigDecimal quantity);

  BigDecimal calculatePurchaseUnitCost(ItemInfo item);

  BigDecimal calculateUnitCost(ItemInfo item);

  UnitKind getPurchaseUnit();

  @JsonIgnore
  String getSummary();

  UnitKind getUnit();

  @JsonIgnore
  boolean isValid();

}
