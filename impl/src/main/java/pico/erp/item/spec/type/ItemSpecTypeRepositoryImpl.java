package pico.erp.item.spec.type;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import kkojaeh.spring.boot.component.Take;
import org.springframework.stereotype.Repository;

@Repository
public class ItemSpecTypeRepositoryImpl implements ItemSpecTypeRepository {

  @Take(required = false)
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

  public static void main(String... args) {
    System.out.println(LocalDateTime.now());
    System.out.println(LocalDateTime.parse("2019-10-18T00:00:00"));
    ;
  }

}
