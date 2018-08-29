package pico.erp.item.data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.data.Auditor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemCategoryHierarchyView implements Comparable<ItemCategoryHierarchyView> {

  ItemCategoryId id;

  ItemCategoryCode code;

  String name;

  ItemCategoryId parentId;

  String path;

  BigDecimal itemCount;

  Auditor createdBy;

  OffsetDateTime createdDate;

  Auditor lastModifiedBy;

  OffsetDateTime lastModifiedDate;

  @Builder.Default
  List<ItemCategoryHierarchyView> children = new LinkedList<>();

  @Override
  public int compareTo(ItemCategoryHierarchyView view) {
    return this.getName().compareTo(view.getName());
  }
}
