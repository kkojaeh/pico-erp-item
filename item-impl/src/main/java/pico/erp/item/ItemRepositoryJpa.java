package pico.erp.item;

import java.time.OffsetDateTime;
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
  long countCreatedBetween(@Param("begin") OffsetDateTime begin,
    @Param("end") OffsetDateTime end);

  @Query("SELECT i FROM Item i WHERE i.code = :code")
  ItemEntity findBy(@Param("code") ItemCode code);

}

@Repository
@Transactional
public class ItemRepositoryJpa implements ItemRepository {

  @Autowired
  private ItemEntityRepository repository;

  @Autowired
  private ItemMapper mapper;

  @Override
  public long countAll() {
    return repository.count();
  }

  @Override
  public long countCreatedBetween(OffsetDateTime begin, OffsetDateTime end) {
    return repository.countCreatedBetween(begin, end);
  }

  @Override
  public Item create(Item item) {
    val entity = mapper.jpa(item);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(ItemId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(ItemId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(ItemCode code) {
    return repository.findBy(code) != null;
  }

  @Override
  public Optional<Item> findBy(ItemId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public Optional<Item> findBy(ItemCode code) {
    return Optional.ofNullable(repository.findBy(code))
      .map(mapper::jpa);
  }

  @Override
  public void update(Item item) {
    val entity = repository.findOne(item.getId());
    mapper.pass(mapper.jpa(item), entity);
    repository.save(entity);
  }

}
