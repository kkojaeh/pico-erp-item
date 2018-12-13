package pico.erp.item;

import java.math.BigDecimal;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import pico.erp.company.CompanyData;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.spec.type.ItemSpecTypeId;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.UnitKind;
import pico.erp.shared.event.Event;

public interface ItemMessages {

  @Data
  class ActivateRequest {

  }

  @Data
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
    ItemCategory category;

    @Valid
    CompanyData customer;

    @Valid
    ItemSpecTypeId specTypeId;

    @NotNull
    UnitKind unit;

    @Min(0)
    BigDecimal baseUnitCost;

    @NotNull
    ItemTypeKind type;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    boolean purchasable;

    @NotNull
    ItemCodeGenerator codeGenerator;


  }

  @Data
  class DeactivateRequest {

  }

  @Data
  class DeleteRequest {

  }

  @Data
  class UpdateRequest {

    @Size(max = TypeDefinitions.NAME_LENGTH)
    @NotNull
    String name;

    @Size(max = TypeDefinitions.EXTERNAL_ID_LENGTH)
    String externalCode;

    @Valid
    ItemCategory category;

    @Valid
    CompanyData customer;

    @Valid
    ItemSpecTypeId specTypeId;

    @NotNull
    UnitKind unit;

    @Min(0)
    BigDecimal baseUnitCost;

    @NotNull
    ItemTypeKind type;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    boolean purchasable;

  }

  @Data
  @AllArgsConstructor
  class SetCategoryRequest {

    @Valid
    ItemCategory category;

  }

  @Data
  @AllArgsConstructor
  class PrepareImportRequest {

    Item previous;

  }

  @Value
  class ActivateResponse {

    Collection<Event> events;

  }

  @Value
  class CreateResponse {

    Collection<Event> events;

  }

  @Value
  class DeactivateResponse {

    Collection<Event> events;

  }

  @Value
  class DeleteResponse {

    Collection<Event> events;

  }

  @Value
  class UpdateResponse {

    Collection<Event> events;

    boolean categoryChanged;

    Item oldItem;

  }

  @Value
  class SetCategoryResponse {

    Collection<Event> events;

    boolean categoryChanged;

  }

  @Value
  class PrepareImportResponse {

    Collection<Event> events;

  }
}
