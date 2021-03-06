package pico.erp.item.category;

import java.util.Optional;
import java.util.stream.Stream;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository {

  long countAll();

  ItemCategory create(@NotNull ItemCategory itemCategory);

  void deleteBy(@NotNull ItemCategoryId id);

  boolean exists(@NotNull ItemCategoryCode code);

  boolean exists(@NotNull ItemCategoryId id);

  Optional<ItemCategory> findBy(@NotNull ItemCategoryCode code);

  Optional<ItemCategory> findBy(@NotNull ItemCategoryId id);

  Stream<ItemCategory> findChildrenBy(@NotNull ItemCategoryId parentId);

  void update(@NotNull ItemCategory itemCategory);

}
