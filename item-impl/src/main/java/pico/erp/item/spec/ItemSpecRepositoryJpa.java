package pico.erp.item.spec;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.ItemId;

@Repository
interface ItemSpecEntityRepository extends CrudRepository<ItemSpecEntity, ItemSpecId> {

  @Query("SELECT spec FROM ItemSpec spec WHERE spec.itemId = :itemId")
  Stream<ItemSpecEntity> findAllBy(@Param("itemId") ItemId itemId);
}

@Repository
@Transactional
public class ItemSpecRepositoryJpa implements ItemSpecRepository {

  @Autowired
  private ItemSpecEntityRepository repository;

  @Autowired
  private ItemSpecMapper mapper;


  @Override
  public ItemSpec create(ItemSpec itemSpec) {
    val entity = mapper.jpa(itemSpec);
    val created = repository.save(entity);
    return mapper.jpa(created);
  }

  @Override
  public void deleteBy(ItemSpecId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(ItemSpecId id) {
    return repository.exists(id);
  }

  @Override
  public Stream<ItemSpec> findAllBy(ItemId itemId) {
    return repository.findAllBy(itemId)
      .map(mapper::jpa);
  }

  @Override
  public Optional<ItemSpec> findBy(ItemSpecId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::jpa);
  }

  @Override
  public void update(ItemSpec itemSpec) {
    val entity = repository.findOne(itemSpec.getId());
    mapper.pass(mapper.jpa(itemSpec), entity);
    repository.save(entity);
  }
}
