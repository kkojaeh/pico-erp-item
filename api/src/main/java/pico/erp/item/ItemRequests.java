package pico.erp.item;

import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pico.erp.company.CompanyId;
import pico.erp.item.category.ItemCategoryId;
import pico.erp.item.spec.type.ItemSpecTypeId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.UnitKind;

public interface ItemRequests {

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class ActivateRequest {

    @Valid
    @NotNull
    ItemId id;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class CreateRequest {

    @Valid
    @NotNull
    ItemId id;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    @NotNull
    String name;

    @Size(max = TypeDefinitions.EXTERNAL_ID_LENGTH)
    String externalCode;

    @Valid
    ItemCategoryId categoryId;

    @Valid
    CompanyId customerId;

    @Valid
    ItemSpecTypeId specTypeId;

    @NotNull
    UnitKind unit;

    @Min(0)
    @NotNull
    BigDecimal baseUnitCost;

    @NotNull
    ItemTypeKind type;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    boolean purchasable;

    @Size(max = TypeDefinitions.EXTERNAL_ID_LENGTH)
    String barcodeNumber;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeactivateRequest {

    @Valid
    @NotNull
    ItemId id;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  class DeleteRequest {

    @Valid
    @NotNull
    ItemId id;

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  class UpdateRequest {

    @Valid
    @NotNull
    ItemId id;

    @Size(max = TypeDefinitions.NAME_LENGTH)
    @NotNull
    String name;

    @Size(max = TypeDefinitions.EXTERNAL_ID_LENGTH)
    String externalCode;

    @NotNull
    UnitKind unit;

    @Min(0)
    @NotNull
    BigDecimal baseUnitCost;

    @NotNull
    ItemTypeKind type;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    @Valid
    ItemCategoryId categoryId;

    @Valid
    CompanyId customerId;

    @Valid
    ItemSpecTypeId specTypeId;

    boolean purchasable;

    @Size(max = TypeDefinitions.EXTERNAL_ID_LENGTH)
    String barcodeNumber;

  }
}
