package pico.erp.item.jpa;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.data.ItemLotCode;
import pico.erp.item.data.ItemLotId;
import pico.erp.item.lot.ItemLot;
import pico.erp.item.lot.ItemLotRepository;

@Repository
interface ItemLotEntityRepository extends CrudRepository<ItemLotEntity, ItemLotId> {

  @Query("SELECT il FROM ItemLot il WHERE il.expired = false AND il.expirationDate IS NOT NULL AND il.expirationDate < :fixedDate")
  Stream<ItemLotEntity> findAllExpireCandidatesBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate);

  @Query("SELECT il FROM ItemLot il WHERE il.code = :code")
  ItemLotEntity findBy(@Param("code") ItemLotCode code);

}

@Repository
@Transactional
public class ItemLotRepositoryJpa implements ItemLotRepository {


  @Autowired
  private ItemLotEntityRepository repository;

  @Autowired
  private ItemJpaMapper mapper;

  @Override
  public ItemLot create(ItemLot itemLot) {
    ItemLotEntity entity = mapper.map(itemLot);
    entity = repository.save(entity);
    return mapper.map(entity);
  }

  @Override
  public void deleteBy(ItemLotId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(ItemLotId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(ItemLotCode code) {
    return repository.findBy(code) != null;
  }

  @Override
  public Stream<ItemLot> findAllExpireCandidatesBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllExpireCandidatesBeforeThan(fixedDate)
      .map(mapper::map);
  }

  @Override
  public Optional<ItemLot> findBy(ItemLotCode code) {
    return Optional.ofNullable(repository.findBy(code))
      .map(mapper::map);
  }

  @Override
  public Optional<ItemLot> findBy(ItemLotId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public void update(ItemLot item) {
    ItemLotEntity entity = repository.findOne(item.getId());
    mapper.pass(mapper.map(item), entity);
    repository.save(entity);
  }
}
