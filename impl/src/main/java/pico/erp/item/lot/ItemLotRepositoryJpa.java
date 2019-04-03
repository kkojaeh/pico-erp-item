package pico.erp.item.lot;

import java.time.LocalDateTime;
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
import pico.erp.item.spec.ItemSpecCode;

@Repository
interface ItemLotEntityRepository extends CrudRepository<ItemLotEntity, ItemLotId> {

  @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM ItemLot l WHERE l.itemId = :itemId AND l.specCode = :specCode AND l.lotCode = :lotCode")
  boolean exists(@Param("itemId") ItemId itemId, @Param("specCode") ItemSpecCode specCode,
    @Param("lotCode") ItemLotCode lotCode);

  @Query("SELECT l FROM ItemLot l WHERE l.expired = false AND l.expirationDate IS NOT NULL AND l.expirationDate < :fixedDate")
  Stream<ItemLotEntity> findAllExpireCandidatesBeforeThan(
    @Param("fixedDate") LocalDateTime fixedDate);

  @Query("SELECT l FROM ItemLot l WHERE l.itemId = :itemId AND l.specCode = :specCode AND l.lotCode = :lotCode")
  ItemLotEntity findBy(@Param("itemId") ItemId itemId, @Param("specCode") ItemSpecCode specCode,
    @Param("lotCode") ItemLotCode lotCode);

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
    repository.deleteById(id);
  }

  @Override
  public boolean exists(ItemLotId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(ItemLotKey key) {
    return repository.exists(key.getItemId(), key.getSpecCode(), key.getLotCode());
  }

  @Override
  public Stream<ItemLot> findAllBy(Iterable<ItemLotId> ids) {
    return StreamSupport.stream(repository.findAllById(ids).spliterator(), false)
      .map(mapper::jpa);
  }

  @Override
  public Stream<ItemLot> findAllExpireCandidatesBeforeThan(LocalDateTime fixedDate) {
    return repository.findAllExpireCandidatesBeforeThan(fixedDate)
      .map(mapper::jpa);
  }

  @Override
  public Optional<ItemLot> findBy(ItemLotId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public Optional<ItemLot> findBy(ItemLotKey key) {
    return Optional
      .ofNullable(repository.findBy(key.getItemId(), key.getSpecCode(), key.getLotCode()))
      .map(mapper::jpa);
  }

  @Override
  public void update(ItemLot item) {
    val entity = repository.findById(item.getId()).get();
    mapper.pass(mapper.jpa(item), entity);
    repository.save(entity);
  }
}
