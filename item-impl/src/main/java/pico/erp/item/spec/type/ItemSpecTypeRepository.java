package pico.erp.item.spec.type;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSpecTypeRepository {

  Stream<ItemSpecType> findAll();

  Optional<ItemSpecType> findBy(ItemSpecTypeId id);

}
