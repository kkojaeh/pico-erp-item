package pico.erp.item.lot;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.item.ItemId;

@Repository
public interface ItemLotRepository {

  ItemLot create(@NotNull ItemLot itemLot);

  void deleteBy(@NotNull ItemLotId id);

  boolean exists(@NotNull ItemLotId id);

  boolean exists(@NotNull ItemId itemId, @NotNull ItemLotCode code);

  Stream<ItemLot> findAllBy(@NotNull Iterable<ItemLotId> ids);

  Stream<ItemLot> findAllExpireCandidatesBeforeThan(@NotNull OffsetDateTime fixedDate);

  Optional<ItemLot> findBy(@NotNull ItemLotId id);

  Optional<ItemLot> findBy(@NotNull ItemId itemId, @NotNull ItemLotCode code);

  void update(@NotNull ItemLot item);

}
