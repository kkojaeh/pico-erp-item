package pico.erp.item.category;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
interface ItemCategoryEntityRepository extends
  CrudRepository<ItemCategoryEntity, ItemCategoryId> {

  @Query("SELECT ic FROM ItemCategory ic WHERE ic.code = :code")
  ItemCategoryEntity findBy(@Param("code") ItemCategoryCode code);

  @Query("SELECT ic FROM ItemCategory ic WHERE ic.parentId = :parentId")
  Stream<ItemCategoryEntity> findChildrenBy(@Param("parentId") ItemCategoryId parentId);

  @Query("SELECT ic FROM ItemCategory ic WHERE ic.parentId IS NULL")
  Stream<ItemCategoryEntity> findRoots();

}

@Repository
@Transactional
public class ItemCategoryRepositoryJpa implements ItemCategoryRepository {

  @Autowired
  private ItemCategoryEntityRepository repository;

  @Autowired
  private ItemCategoryMapper mapper;

  @Override
  public long countAll() {
    return repository.count();
  }

  @Override
  public ItemCategory create(ItemCategory itemCategory) {
    val entity = mapper.jpa(itemCategory);
    val created = repository.save(entity);
    return mapper.jpa(created);
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
      .map(mapper::jpa);
  }

  @Override
  public Optional<ItemCategory> findBy(ItemCategoryCode code) {
    return Optional.ofNullable(repository.findBy(code))
      .map(mapper::jpa);
  }

  @Override
  public Stream<ItemCategory> findChildrenBy(ItemCategoryId parentId) {
    return Optional.ofNullable(parentId)
      .map(id -> repository.findChildrenBy(id))
      .orElseGet(() -> repository.findRoots())
      .map(mapper::jpa);
  }

  @Override
  public void update(ItemCategory itemCategory) {
    val entity = repository.findOne(itemCategory.getId());
    mapper.pass(mapper.jpa(itemCategory), entity);
    repository.save(entity);
  }
}
