package pico.erp.item.core;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.item.data.ItemSpecId;
import pico.erp.item.domain.ItemSpec;

@Repository
public interface ItemSpecRepository {

  ItemSpec create(@NotNull ItemSpec itemSpec);

  void deleteBy(@NotNull ItemSpecId id);

  boolean exists(@NotNull ItemSpecId id);

  Optional<ItemSpec> findBy(@NotNull ItemSpecId id);

  void update(@NotNull ItemSpec itemSpec);

}
