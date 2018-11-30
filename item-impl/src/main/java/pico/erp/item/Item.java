package pico.erp.item;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.attachment.AttachmentId;
import pico.erp.audit.annotation.Audit;
import pico.erp.company.CompanyData;
import pico.erp.item.ItemEvents.CategoryChangedEvent;
import pico.erp.item.ItemEvents.CreatedEvent;
import pico.erp.item.ItemEvents.DeletedEvent;
import pico.erp.item.ItemEvents.UpdatedEvent;
import pico.erp.item.ItemExceptions.CannotActivateException;
import pico.erp.item.ItemExceptions.CannotDeactivateException;
import pico.erp.item.ItemExceptions.CannotSpecifyException;
import pico.erp.item.ItemMessages.ActivateResponse;
import pico.erp.item.ItemMessages.CreateResponse;
import pico.erp.item.ItemMessages.DeactivateResponse;
import pico.erp.item.ItemMessages.DeleteResponse;
import pico.erp.item.ItemMessages.SetCategoryResponse;
import pico.erp.item.ItemMessages.UpdateResponse;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.spec.ItemSpecVariables;
import pico.erp.item.spec.type.ItemSpecType;
import pico.erp.shared.data.UnitKind;
import pico.erp.shared.event.Event;

@Getter
@ToString
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Audit(alias = "item")
public class Item implements Serializable, ItemInfo {

  private static final long serialVersionUID = 1L;

  @Id
  ItemId id;

  ItemCode code;

  String externalCode;

  ItemCategory category;

  String name;

  UnitKind unit;

  /**
   * 원자재 또는 구매 자재의 경우 단가 공정으로 인한 금액은 포함 되지 않는다
   */
  BigDecimal baseUnitCost;

  ItemTypeKind type;

  ItemStatusKind status;

  @Transient
  ItemSpecType specType;

  String description;

  CompanyData customer;

  boolean purchasable;

  AttachmentId attachmentId;

  public Item() {
    this.status = ItemStatusKind.DRAFT;
  }

  public ActivateResponse apply(ItemMessages.ActivateRequest request) {
    if (!status.isActivatable()) {
      throw new CannotActivateException();
    }
    status = ItemStatusKind.ACTIVATED;
    return new ActivateResponse(
      Collections.emptyList()
    );
  }

  public CreateResponse apply(ItemMessages.CreateRequest request) {
    id = request.getId();
    name = request.getName();
    externalCode = request.getExternalCode();
    unit = request.getUnit();
    baseUnitCost = request.getBaseUnitCost();
    type = request.getType();
    description = request.getDescription();
    purchasable = request.isPurchasable();
    apply(new ItemMessages.SetCategoryRequest(request.getCategory()));
    customer = request.getCustomer();
    specType = request.getSpecType();
    attachmentId = request.getAttachmentId();
    code = request.getCodeGenerator().generate(this);
    return new CreateResponse(
      Arrays.asList(new CreatedEvent(this.id))
    );
  }

  public DeactivateResponse apply(ItemMessages.DeactivateRequest request) {
    if (!status.isDeactivatable()) {
      throw new CannotDeactivateException();
    }
    status = ItemStatusKind.DEACTIVATED;
    return new DeactivateResponse(
      Collections.emptyList()
    );
  }

  public DeleteResponse apply(ItemMessages.DeleteRequest request) {
    return new DeleteResponse(
      Arrays.asList(new DeletedEvent(this.id))
    );
  }

  public UpdateResponse apply(ItemMessages.UpdateRequest request) {
    List<Event> events = new LinkedList<>();
    Item old = toBuilder().build();
    name = request.getName();
    externalCode = request.getExternalCode();
    unit = request.getUnit();
    baseUnitCost = request.getBaseUnitCost();
    type = request.getType();
    description = request.getDescription();
    purchasable = request.isPurchasable();
    val setCategoryResponse = apply(new ItemMessages.SetCategoryRequest(request.getCategory()));
    events.addAll(setCategoryResponse.getEvents());
    customer = request.getCustomer();
    specType = request.getSpecType();
    attachmentId = request.getAttachmentId();
    events.add(new UpdatedEvent(this.id));
    return new UpdateResponse(events,
      setCategoryResponse.isCategoryChanged(),
      old
    );
  }

  private SetCategoryResponse apply(ItemMessages.SetCategoryRequest request) {
    val oldCategory = this.category;
    val newCategory = request.getCategory();
    if (Optional.ofNullable(oldCategory)
      .equals(Optional.ofNullable(newCategory))) {
      return new SetCategoryResponse(Collections.emptyList(), false);
    }
    if (oldCategory != null) {
      oldCategory.removeItem(this);
    }
    if (newCategory != null) {
      newCategory.addItem(this);
    }
    this.category = newCategory;
    val oldCategoryId = Optional.ofNullable(oldCategory)
      .map(c -> c.getId())
      .orElse(null);
    val newCategoryId = Optional.ofNullable(newCategory)
      .map(c -> c.getId())
      .orElse(null);
    return new SetCategoryResponse(
      Arrays.asList(new CategoryChangedEvent(this.id, oldCategoryId, newCategoryId)),
      true
    );
  }

  public ItemSpecVariables createSpecVariables() {
    if (!isSpecifiable()) {
      throw new CannotSpecifyException();
    }
    return specType.create();
  }

  public Serializable getSpecMetadata() {
    if (!isSpecifiable()) {
      throw new CannotSpecifyException();
    }
    return specType.getMetadata();
  }

  public boolean isSpecifiable() {
    return specType != null;
  }

  public boolean isSalable() {
    return type.isSalable();
  }

}
