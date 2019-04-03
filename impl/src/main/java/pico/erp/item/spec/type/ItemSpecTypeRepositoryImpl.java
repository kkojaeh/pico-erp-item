package pico.erp.item.spec.type;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import kkojaeh.spring.boot.component.ComponentAutowired;
import org.springframework.stereotype.Repository;

@Repository
public class ItemSpecTypeRepositoryImpl implements ItemSpecTypeRepository {

  @ComponentAutowired(required = false)
  private Set<ItemSpecType> types;

  @Override
  public Stream<ItemSpecType> findAll() {
    return types.stream();
  }

  @Override
  public Optional<ItemSpecType> findBy(ItemSpecTypeId id) {
    return types.stream()
      .filter(type -> type.getId().equals(id))
      .findFirst();
  }

}
