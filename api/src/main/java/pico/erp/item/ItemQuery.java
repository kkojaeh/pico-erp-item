package pico.erp.item;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pico.erp.shared.data.LabeledValuable;

public interface ItemQuery {

  List<? extends LabeledValuable> asLabels(@NotNull String keyword, long limit);

  Page<ItemView> retrieve(@NotNull ItemView.Filter filter, @NotNull Pageable pageable);

}
