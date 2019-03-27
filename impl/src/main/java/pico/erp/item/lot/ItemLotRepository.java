package pico.erp.item.lot;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemLotRepository {

  ItemLot create(@NotNull ItemLot itemLot);

  void deleteBy(@NotNull ItemLotId id);

  boolean exists(@NotNull ItemLotId id);

  boolean exists(@NotNull ItemLotKey key);

  Stream<ItemLot> findAllBy(@NotNull Iterable<ItemLotId> ids);

  Stream<ItemLot> findAllExpireCandidatesBeforeThan(@NotNull LocalDateTime fixedDate);

  Optional<ItemLot> findBy(@NotNull ItemLotId id);

  Optional<ItemLot> findBy(@NotNull ItemLotKey key);

  void update(@NotNull ItemLot item);

}
