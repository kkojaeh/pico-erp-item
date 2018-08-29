package pico.erp.item;

import java.util.List;
import javax.validation.constraints.NotNull;
import pico.erp.shared.data.LabeledValuable;

public interface ItemSpecTypeQuery {

  List<? extends LabeledValuable> asLabels(@NotNull String keyword, long limit);

}
