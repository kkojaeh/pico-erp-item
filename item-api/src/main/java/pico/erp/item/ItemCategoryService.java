package pico.erp.item;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.ItemCategoryRequests.CreateRequest;
import pico.erp.item.ItemCategoryRequests.DeleteRequest;
import pico.erp.item.ItemCategoryRequests.UpdateRequest;
import pico.erp.item.data.ItemCategoryCode;
import pico.erp.item.data.ItemCategoryData;
import pico.erp.item.data.ItemCategoryId;

public interface ItemCategoryService {

  ItemCategoryData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull ItemCategoryCode code);

  boolean exists(@NotNull ItemCategoryId id);

  ItemCategoryData get(@NotNull ItemCategoryCode code);

  ItemCategoryData get(@NotNull ItemCategoryId id);

  void update(@Valid UpdateRequest request);

}
