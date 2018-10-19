package pico.erp.item;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface ItemEntityRepository extends CrudRepository<ItemEntity, ItemId> {

  @Query("SELECT COUNT(i) FROM Item i WHERE i.createdDate >= :begin AND i.createdDate <= :end")
  long countByCreatedDateBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

  @Query("SELECT i FROM Item i WHERE i.code = :code")
  ItemEntity findBy(@Param("code") ItemCode code);

}

@Repository
@Transactional
public class ItemRepositoryJpa implements ItemRepository {

  @Autowired
  private ItemEntityRepository itemEntityRepository;

  @Autowired
  private ItemMapper mapperapper;

  @Override
  public long countAll() {
    return itemEntityRepository.count();
  }

  @Override
  public long countByCreatedThisMonth() {
    val begin = OffsetDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
      .with(LocalTime.MIN);
    val end = OffsetDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
      .with(LocalTime.MAX);
    return itemEntityRepository.countByCreatedDateBetween(begin, end);
  }

  @Override
  public Item create(Item item) {
    val entity = mapperapper.entity(item);
    val created = itemEntityRepository.save(entity);
    return mapperapper.domain(created);
  }

  @Override
  public void deleteBy(ItemId id) {
    itemEntityRepository.delete(id);
  }

  @Override
  public boolean exists(ItemId id) {
    return itemEntityRepository.exists(id);
  }

  @Override
  public boolean exists(ItemCode code) {
    return itemEntityRepository.findBy(code) != null;
  }

  @Override
  public Optional<Item> findBy(ItemId id) {
    return Optional.ofNullable(itemEntityRepository.findOne(id))
      .map(mapperapper::domain);
  }

  @Override
  public Optional<Item> findBy(ItemCode code) {
    return Optional.ofNullable(itemEntityRepository.findBy(code))
      .map(mapperapper::domain);
  }

  @Override
  public void update(Item item) {
    val entity = itemEntityRepository.findOne(item.getId());
    mapperapper.pass(mapperapper.entity(item), entity);
    itemEntityRepository.save(entity);
  }

}