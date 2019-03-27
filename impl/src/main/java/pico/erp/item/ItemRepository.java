package pico.erp.item;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository {

  long countAll();

  long countCreatedBetween(LocalDateTime begin, LocalDateTime end);

  Item create(@NotNull Item item);

  void deleteBy(@NotNull ItemId id);

  boolean exists(@NotNull ItemId id);

  boolean exists(@NotNull ItemCode code);

  Optional<Item> findBy(@NotNull ItemId id);

  Optional<Item> findBy(@NotNull ItemCode code);

  Stream<Item> getAll();

  void update(@NotNull Item item);

}
