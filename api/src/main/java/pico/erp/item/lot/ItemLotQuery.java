package pico.erp.item.lot;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemLotQuery {

  Page<ItemLotView> retrieve(@NotNull ItemLotView.Filter filter, @NotNull Pageable pageable);

}
