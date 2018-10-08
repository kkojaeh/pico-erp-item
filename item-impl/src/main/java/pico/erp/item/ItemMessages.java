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
import pico.erp.attachment.AttachmentId;
import pico.erp.company.CompanyData;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.code.ItemCodeGenerator;
import pico.erp.item.spec.type.ItemSpecType;
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
    ItemSpecType specType;

    @NotNull
    UnitKind unit;

    @Min(0)
    BigDecimal baseUnitCost;

    @NotNull
    ItemTypeKind type;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    boolean purchasable;

    boolean sellable;

    @Valid
    AttachmentId attachmentId;

    @NotNull
    ItemCodeGenerator itemCodeGenerator;

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
    ItemSpecType specType;

    @NotNull
    UnitKind unit;

    @Min(0)
    BigDecimal baseUnitCost;

    @NotNull
    ItemTypeKind type;

    @Size(max = TypeDefinitions.DESCRIPTION_LENGTH)
    String description;

    boolean purchasable;

    boolean sellable;

    @Valid
    AttachmentId attachmentId;

    @NotNull
    ItemCodeGenerator itemCodeGenerator;

  }

  @Data
  @AllArgsConstructor
  class SetCategoryRequest {

    @Valid
    ItemCategory category;

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
}
