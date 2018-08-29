package pico.erp.item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemSpecRequests.CreateRequest;
import pico.erp.item.ItemSpecRequests.DeleteRequest;
import pico.erp.item.ItemSpecRequests.UpdateRequest;
import pico.erp.item.data.ItemSpecData;
import pico.erp.item.data.ItemSpecId;

public interface ItemSpecService {

  ItemSpecData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull ItemSpecId id);

  ItemSpecData get(@NotNull ItemSpecId id);

  void update(@Valid UpdateRequest request);

}
