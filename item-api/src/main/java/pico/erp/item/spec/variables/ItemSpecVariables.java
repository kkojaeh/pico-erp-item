package pico.erp.item.spec.variables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import pico.erp.item.ItemInfo;

@JsonTypeInfo(use = Id.CLASS, property = "@type")
public interface ItemSpecVariables extends Serializable {

  @JsonIgnore
  String getSummary();

  @JsonIgnore
  boolean isValid();

  BigDecimal calculateUnitCost(ItemInfo item);

}
