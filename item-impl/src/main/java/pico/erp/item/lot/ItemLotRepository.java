package pico.erp.item.lot;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;
import pico.erp.item.data.ItemLotCode;
import pico.erp.item.data.ItemLotId;

@Repository
public interface ItemLotRepository {

  ItemLot create(@NotNull ItemLot itemLot);

  void deleteBy(@NotNull ItemLotId id);

  boolean exists(@NotNull ItemLotId id);

  boolean exists(@NotNull ItemLotCode code);

  Stream<ItemLot> findAllExpireCandidatesBeforeThan(@NotNull OffsetDateTime fixedDate);

  Optional<ItemLot> findBy(@NotNull ItemLotCode code);

  Optional<ItemLot> findBy(@NotNull ItemLotId id);

  void update(@NotNull ItemLot item);

}
