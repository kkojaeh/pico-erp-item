package pico.erp.item.spec.type;

import kkojaeh.spring.boot.component.Give;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Give
@Transactional
@Validated
public class ItemSpecTypeServiceLogic implements ItemSpecTypeService {

  @Autowired
  private ItemSpecTypeRepository itemSpecTypeRepository;

  @Autowired
  private ItemSpecTypeMapper mapper;

  @Override
  public ItemSpecTypeData get(ItemSpecTypeId id) {
    return itemSpecTypeRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ItemSpecTypeExceptions.NotFoundException::new);
  }
}
