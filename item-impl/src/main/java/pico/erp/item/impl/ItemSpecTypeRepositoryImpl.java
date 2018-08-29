package pico.erp.item.impl;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import pico.erp.item.core.ItemSpecTypeRepository;
import pico.erp.item.data.ItemSpecType;
import pico.erp.item.data.ItemSpecTypeId;

@Repository
public class ItemSpecTypeRepositoryImpl implements ItemSpecTypeRepository {

  @Lazy
  @Autowired(required = false)
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
