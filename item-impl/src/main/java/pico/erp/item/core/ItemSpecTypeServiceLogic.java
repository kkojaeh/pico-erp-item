package pico.erp.item.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.ItemSpecTypeExceptions.NotFoundException;
import pico.erp.item.ItemSpecTypeService;
import pico.erp.item.data.ItemSpecTypeData;
import pico.erp.item.data.ItemSpecTypeId;
import pico.erp.shared.Public;

@Service
@Public
@Transactional
@Validated
public class ItemSpecTypeServiceLogic implements ItemSpecTypeService {

  @Autowired
  private ItemSpecTypeRepository itemSpecTypeRepository;

  @Autowired
  private ItemMapper mapper;

  @Override
  public ItemSpecTypeData get(ItemSpecTypeId id) {
    return itemSpecTypeRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }
}
