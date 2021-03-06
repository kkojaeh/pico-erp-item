package pico.erp.item.spec;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.item.ItemId;

@Repository
public interface ItemSpecRepository {

  ItemSpec create(@NotNull ItemSpec itemSpec);

  void deleteBy(@NotNull ItemSpecId id);

  boolean exists(@NotNull ItemSpecId id);

  Stream<ItemSpec> findAllBy(@NotNull ItemId itemId);

  Optional<ItemSpec> findBy(@NotNull ItemSpecId id);

  void update(@NotNull ItemSpec itemSpec);

}
