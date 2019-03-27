package pico.erp.item;

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

@Repository
interface ItemEntityRepository extends CrudRepository<ItemEntity, ItemId> {

  @Query("SELECT COUNT(i) FROM Item i WHERE i.createdDate >= :begin AND i.createdDate <= :end")
  long countCreatedBetween(@Param("begin") LocalDateTime begin,
    @Param("end") LocalDateTime end);

  @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM Item i WHERE i.code = :code")
  boolean exists(@Param("code") ItemCode code);

  @Query("SELECT i FROM Item i WHERE i.code = :code")
  Optional<ItemEntity> findBy(@Param("code") ItemCode code);

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
  public long countCreatedBetween(LocalDateTime begin, LocalDateTime end) {
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
    repository.deleteById(id);
  }

  @Override
  public boolean exists(ItemId id) {
    return repository.existsById(id);
  }

  @Override
  public boolean exists(ItemCode code) {
    return repository.exists(code);
  }

  @Override
  public Optional<Item> findBy(ItemId id) {
    return repository.findById(id)
      .map(mapper::jpa);
  }

  @Override
  public Optional<Item> findBy(ItemCode code) {
    return repository.findBy(code)
      .map(mapper::jpa);
  }

  @Override
  public Stream<Item> getAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
      .map(mapper::jpa);
  }

  @Override
  public void update(Item item) {
    val entity = repository.findById(item.getId()).get();
    mapper.pass(mapper.jpa(item), entity);
    repository.save(entity);
  }

}
