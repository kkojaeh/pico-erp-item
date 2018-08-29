package pico.erp.item.core;

import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Repository;
import pico.erp.item.data.ItemSpecType;
import pico.erp.item.data.ItemSpecTypeId;

@Repository
public interface ItemSpecTypeRepository {

  Stream<ItemSpecType> findAll();

  Optional<ItemSpecType> findBy(ItemSpecTypeId id);

}
