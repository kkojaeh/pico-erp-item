package pico.erp.item.impl;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.core.ItemSpecRepository;
import pico.erp.item.data.ItemSpecId;
import pico.erp.item.domain.ItemSpec;
import pico.erp.item.impl.jpa.ItemSpecEntity;

@Repository
interface ItemSpecEntityRepository extends CrudRepository<ItemSpecEntity, ItemSpecId> {

}

@Repository
@Transactional
public class ItemSpecRepositoryJpa implements ItemSpecRepository {

  @Autowired
  private ItemSpecEntityRepository repository;

  @Autowired
  private ItemJpaMapper mapper;


  @Override
  public ItemSpec create(ItemSpec itemSpec) {
    ItemSpecEntity entity = mapper.map(itemSpec);
    entity = repository.save(entity);
    return mapper.map(entity);
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
      .map(mapper::map);
  }

  @Override
  public void update(ItemSpec itemSpec) {
    ItemSpecEntity entity = repository.findOne(itemSpec.getId());
    mapper.pass(mapper.map(itemSpec), entity);
    repository.save(entity);
  }
}
