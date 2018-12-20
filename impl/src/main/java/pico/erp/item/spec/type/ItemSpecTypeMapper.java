package pico.erp.item.spec.type;

import java.util.Optional;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

@Mapper
public abstract class ItemSpecTypeMapper {

  @Lazy
  @Autowired
  protected ItemSpecTypeRepository itemSpecTypeRepository;

  public abstract ItemSpecTypeData map(ItemSpecType type);

  public ItemSpecType map(ItemSpecTypeId itemSpecTypeId) {
    return Optional.ofNullable(itemSpecTypeId)
      .map(id -> itemSpecTypeRepository.findBy(id)
        .orElseThrow(ItemSpecTypeExceptions.NotFoundException::new)
      )
      .orElse(null);
  }

}
