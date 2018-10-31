package pico.erp.item.spec;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface ItemSpecService {

  ItemSpecData create(@Valid ItemSpecRequests.CreateRequest request);

  void delete(@Valid ItemSpecRequests.DeleteRequest request);

  boolean exists(@NotNull ItemSpecId id);

  ItemSpecData get(@NotNull ItemSpecId id);

  void lock(@Valid ItemSpecRequests.LockRequest request);

  void unlock(@Valid ItemSpecRequests.UnlockRequest request);

  void update(@Valid ItemSpecRequests.UpdateRequest request);

}
