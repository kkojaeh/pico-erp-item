package pico.erp.item.data;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.company.data.CompanyId;
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

  boolean sellable;

  boolean specifiable;

  AttachmentId attachmentId;

}
