package pico.erp.item.core;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.item.data.ItemCode;
import pico.erp.item.data.ItemId;
import pico.erp.item.domain.Item;

@Repository
public interface ItemRepository {

  long countAll();

  long countByCreatedThisMonth();

  Item create(@NotNull Item item);

  void deleteBy(@NotNull ItemId id);

  boolean exists(@NotNull ItemId id);

  boolean exists(@NotNull ItemCode code);

  Optional<Item> findBy(@NotNull ItemId id);

  Optional<Item> findBy(@NotNull ItemCode code);

  void update(@NotNull Item item);

}
