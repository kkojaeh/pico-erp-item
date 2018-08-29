package pico.erp.item.data;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.data.CompanyId;
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

  ItemCategoryId categoryId;

  String categoryName;

  String categoryPath;

  CompanyId customerId;

  String customerName;

  ItemStatusKind status;

  Auditor createdBy;

  OffsetDateTime createdDate;

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
  }

}
