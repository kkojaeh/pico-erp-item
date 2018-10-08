package pico.erp.item.category;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import pico.erp.item.category.ItemCategoryRequests.CreateRequest;
import pico.erp.item.category.ItemCategoryRequests.DeleteRequest;
import pico.erp.item.category.ItemCategoryRequests.UpdateRequest;

public interface ItemCategoryService {

  ItemCategoryData create(@Valid CreateRequest request);

  void delete(@Valid DeleteRequest request);

  boolean exists(@NotNull ItemCategoryCode code);

  boolean exists(@NotNull ItemCategoryId id);

  ItemCategoryData get(@NotNull ItemCategoryCode code);

  ItemCategoryData get(@NotNull ItemCategoryId id);

  void update(@Valid UpdateRequest request);

}
