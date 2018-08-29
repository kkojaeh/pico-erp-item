package pico.erp.item.impl;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.core.ItemCategoryRepository;
import pico.erp.item.data.ItemCategoryCode;
import pico.erp.item.data.ItemCategoryId;
import pico.erp.item.domain.ItemCategory;
import pico.erp.item.impl.jpa.ItemCategoryEntity;

@Repository
interface ItemCategoryEntityRepository extends
  CrudRepository<ItemCategoryEntity, ItemCategoryId> {

  @Query("SELECT ic FROM ItemCategory ic WHERE ic.code = :code")
  ItemCategoryEntity findBy(@Param("code") ItemCategoryCode code);

  @Query("SELECT ic FROM ItemCategory ic WHERE ic.parentId = :parentId")
  Stream<ItemCategoryEntity> findByParentId(@Param("parentId") ItemCategoryId parentId);

}

@Repository
@Transactional
public class ItemCategoryRepositoryJpa implements ItemCategoryRepository {

  @Autowired
  private ItemCategoryEntityRepository repository;

  @Autowired
  private ItemJpaMapper mapper;

  @Override
  public long countAll() {
    return repository.count();
  }

  @Override
  public ItemCategory create(ItemCategory itemCategory) {
    ItemCategoryEntity entity = mapper.map(itemCategory);
    entity = repository.save(entity);
    return mapper.map(entity);
  }

  @Override
  public void deleteBy(ItemCategoryId id) {
    repository.delete(id);
  }

  @Override
  public boolean exists(ItemCategoryId id) {
    return repository.exists(id);
  }

  @Override
  public boolean exists(ItemCategoryCode code) {
    return repository.findBy(code) != null;
  }

  @Override
  public Optional<ItemCategory> findBy(ItemCategoryId id) {
    return Optional.ofNullable(repository.findOne(id))
      .map(mapper::map);
  }

  @Override
  public Optional<ItemCategory> findBy(ItemCategoryCode code) {
    return Optional.ofNullable(repository.findBy(code))
      .map(mapper::map);
  }

  @Override
  public Stream<ItemCategory> findChildrenBy(ItemCategoryId parentId) {
    return repository.findByParentId(parentId)
      .map(mapper::map);
  }

  @Override
  public void update(ItemCategory itemCategory) {
    ItemCategoryEntity entity = repository.findOne(itemCategory.getId());
    mapper.pass(mapper.map(itemCategory), entity);
    repository.save(entity);
  }
}
