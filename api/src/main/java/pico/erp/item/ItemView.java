package pico.erp.item;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.category.ItemCategoryId;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.data.UnitKind;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemView {

  ItemId id;

  ItemCode code;

  String name;

  UnitKind unit;

  ItemTypeKind type;

  String externalCode;

  String barcodeNumber;

  ItemCategoryId categoryId;

  CompanyId customerId;

  ItemStatusKind status;

  Auditor createdBy;

  LocalDateTime createdDate;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Filter {

    String name;

    /**
     * code 와 externalCode 모두 검색
     */
    String code;

    ItemCategoryId categoryId;

    CompanyId customerId;

    List<ItemStatusKind> statuses;

    List<ItemTypeKind> types;

    Boolean purchasable;

    Boolean salable;
  }

}
