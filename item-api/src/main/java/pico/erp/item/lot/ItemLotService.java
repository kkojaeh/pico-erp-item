package pico.erp.item.lot;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.lot.ItemLotRequests.CreateRequest;
import pico.erp.item.lot.ItemLotRequests.DeleteRequest;
import pico.erp.item.lot.ItemLotRequests.ExpireRequest;
import pico.erp.item.lot.ItemLotRequests.UpdateRequest;

public interface ItemLotService {

  ItemLotData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull ItemLotCode code);

  boolean exists(@NotNull ItemLotId id);

  void expire(@Valid @NotNull ExpireRequest request);

  ItemLotData get(@NotNull ItemLotCode code);

  ItemLotData get(@NotNull ItemLotId id);

  void update(@Valid UpdateRequest request);


}
