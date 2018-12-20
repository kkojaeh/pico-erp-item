package pico.erp.item.spec;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface ItemSpecService {

  BigDecimal calculate(@Valid @NotNull ItemSpecRequests.CalculatePurchaseQuantityRequest request);

  ItemSpecData create(@Valid @NotNull ItemSpecRequests.CreateRequest request);

  void delete(@Valid @NotNull ItemSpecRequests.DeleteRequest request);

  boolean exists(@NotNull ItemSpecId id);

  ItemSpecData get(@NotNull ItemSpecId id);

  void lock(@Valid @NotNull ItemSpecRequests.LockRequest request);

  void unlock(@Valid @NotNull ItemSpecRequests.UnlockRequest request);

  void update(@Valid @NotNull ItemSpecRequests.UpdateRequest request);

}
