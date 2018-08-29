package pico.erp.item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemLotRequests.CreateRequest;
import pico.erp.item.ItemLotRequests.DeleteRequest;
import pico.erp.item.ItemLotRequests.ExpireRequest;
import pico.erp.item.ItemLotRequests.UpdateRequest;
import pico.erp.item.data.ItemLotCode;
import pico.erp.item.data.ItemLotData;
import pico.erp.item.data.ItemLotId;

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
