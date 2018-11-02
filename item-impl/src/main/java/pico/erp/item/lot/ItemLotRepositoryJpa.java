package pico.erp.item.lot;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.ItemId;

@Repository
interface ItemLotEntityRepository extends CrudRepository<ItemLotEntity, ItemLotId> {

  @Query("SELECT il FROM ItemLot il WHERE il.expired = false AND il.expirationDate IS NOT NULL AND il.expirationDate < :fixedDate")
  Stream<ItemLotEntity> findAllExpireCandidatesBeforeThan(
    @Param("fixedDate") OffsetDateTime fixedDate);

  @Query("SELECT CASE WHEN COUNT(il) > 0 THEN true ELSE false END FROM ItemLot il WHERE il.itemId = :itemId AND il.code = :code")
  boolean exists(@Param("itemId") ItemId itemId, @Param("code") ItemLotCode code);

  @Query("SELECT il FROM ItemLot il WHERE il.itemId = :itemId AND il.code = :code")
  ItemLotEntity findBy(@Param("itemId") ItemId itemId, @Param("code") ItemLotCode code);

}

@Repository
@Transactional
public class ItemLotRepositoryJpa implements ItemLotRepository {


  @Autowired
  private ItemLotEntityRepository repository;

  @Autowired
  private ItemLotMapper mapper;

  @Override
  public ItemLot create(ItemLot itemLot) {
    val entity = mapper.jpa(itemLot);
    val created = repository.save(entity);
    return mapper.jpa(created);
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
  public boolean exists(ItemId itemId, ItemLotCode code) {
    return repository.exists(itemId, code);
  }

  @Override
  public Stream<ItemLot> findAllBy(Iterable<ItemLotId> ids) {
    return StreamSupport.stream(repository.findAll(ids).spliterator(), false)
      .map(mapper::jpa);
  }

  @Override
  public Stream<ItemLot> findAllExpireCandidatesBeforeThan(OffsetDateTime fixedDate) {
    return repository.findAllExpireCandidatesBeforeThan(fixedDate)
      .map(mapper::jpa);
  }

  @Override
  public Optional<ItemLot> findBy(ItemLotId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public Optional<ItemLot> findBy(ItemId itemId, ItemLotCode code) {
    return Optional.ofNullable(repository.findBy(itemId, code))
      .map(mapper::jpa);
  }

  @Override
  public void update(ItemLot item) {
    val entity = repository.findOne(item.getId());
    mapper.pass(mapper.jpa(item), entity);
    repository.save(entity);
  }
}
