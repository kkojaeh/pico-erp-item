package pico.erp.item.spec.type;

import javax.validation.constraints.NotNull;

public interface ItemSpecTypeService {

  ItemSpecTypeData get(@NotNull ItemSpecTypeId id);

}
