package pico.erp.item;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.company.CompanyId;
import pico.erp.item.category.ItemCategoryId;
import pico.erp.item.spec.type.ItemSpecTypeId;
import pico.erp.shared.data.UnitKind;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class ItemData implements Serializable, ItemInfo {

  private static final long serialVersionUID = 1L;

  @Id
  ItemId id;

  ItemCode code;

  String externalCode;

  ItemCategoryId categoryId;

  ItemSpecTypeId specTypeId;

  String name;

  UnitKind unit;

  BigDecimal baseUnitCost;

  ItemTypeKind type;

  String description;

  CompanyId customerId;

  ItemStatusKind status;

  boolean purchasable;

  boolean salable;

  boolean specifiable;

}
