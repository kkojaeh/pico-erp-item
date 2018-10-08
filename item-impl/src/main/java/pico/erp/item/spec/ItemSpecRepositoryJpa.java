package pico.erp.item.spec;

import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface ItemSpecEntityRepository extends CrudRepository<ItemSpecEntity, ItemSpecId> {

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
    val entity = mapper.entity(itemSpec);
    val created = repository.save(entity);
    return mapper.domain(created);
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
  public Optional<ItemSpec> findBy(ItemSpecId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::domain);
  }

  @Override
  public void update(ItemSpec itemSpec) {
    val entity = repository.findOne(itemSpec.getId());
    mapper.pass(mapper.entity(itemSpec), entity);
    repository.save(entity);
  }
}
