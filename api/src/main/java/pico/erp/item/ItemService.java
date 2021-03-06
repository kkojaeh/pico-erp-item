package pico.erp.item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemRequests.ActivateRequest;
import pico.erp.item.ItemRequests.CreateRequest;
import pico.erp.item.ItemRequests.DeactivateRequest;
import pico.erp.item.ItemRequests.DeleteRequest;
import pico.erp.item.ItemRequests.UpdateRequest;

public interface ItemService {

  void activate(@Valid ActivateRequest request);

  ItemData create(@Valid CreateRequest request);

  void deactivate(@Valid DeactivateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull ItemCode code);

  boolean exists(@NotNull ItemId id);

  ItemData get(@NotNull ItemCode code);

  ItemData get(@NotNull ItemId id);

  void update(@Valid UpdateRequest request);

}
