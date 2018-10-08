package pico.erp.item.category;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.shared.TypeDefinitions;

public interface ItemCategoryRequests {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    ItemCategoryId id;

    @Valid
    ItemCategoryId parentId;

    @Pattern(regexp = TypeDefinitions.PATH_NAME_REGEXP)
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    ItemCategoryId id;

    @Valid
    ItemCategoryId parentId;

    @Pattern(regexp = TypeDefinitions.PATH_NAME_REGEXP)
    @Size(max = TypeDefinitions.NAME_LENGTH)
    String name;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    ItemCategoryId id;

  }
}
