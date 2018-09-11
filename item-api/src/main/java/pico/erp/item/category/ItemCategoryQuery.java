package pico.erp.item.category;

import java.util.List;
import javax.validation.constraints.NotNull;
import pico.erp.item.data.ItemCategoryHierarchyView;
import pico.erp.shared.data.LabeledValuable;

public interface ItemCategoryQuery {

  List<? extends LabeledValuable> asLabels(@NotNull String keyword, long limit);

  List<ItemCategoryHierarchyView> findAllAsHierarchy();

}
