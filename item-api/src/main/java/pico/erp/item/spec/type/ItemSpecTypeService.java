package pico.erp.item.spec.type;

import javax.validation.constraints.NotNull;
import pico.erp.item.data.ItemSpecTypeData;
import pico.erp.item.data.ItemSpecTypeId;

public interface ItemSpecTypeService {

  ItemSpecTypeData get(@NotNull ItemSpecTypeId id);

}
