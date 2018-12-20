package pico.erp.item.category;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import lombok.val;
import pico.erp.audit.annotation.Audit;
import pico.erp.item.Item;
import pico.erp.item.category.ItemCategoryEvents.UpdatedEvent;
import pico.erp.shared.event.Event;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Audit(alias = "item-category")
public class ItemCategory implements Serializable {

  private static final long serialVersionUID = 1L;

  private static String PATH_DELIMITER = " > ";

  private static String KEY_DELIMITER = ">";

  @Id
  ItemCategoryId id;

  ItemCategoryCode code;

  String name;

  ItemCategory parent;

  String path;

  String key;

  BigDecimal itemCount;

  String description;

  public ItemCategory() {
    this.itemCount = BigDecimal.ZERO;
  }

  public void addItem(Item item) {
    itemCount = itemCount.add(BigDecimal.ONE);
  }

  public ItemCategoryMessages.CreateResponse apply(ItemCategoryMessages.CreateRequest request) {
    this.id = request.getId();
    this.name = request.getName();
    this.description = request.getDescription();
    this.parent = request.getParent();
    this.apply(new ItemCategoryMessages.SetParentRequest(request.getParent()));
    this.code = request.getCodeGenerator().generate(this);
    return new ItemCategoryMessages.CreateResponse(
      Arrays.asList(new ItemCategoryEvents.CreatedEvent(this.id))
    );
  }

  public ItemCategoryMessages.UpdateResponse apply(ItemCategoryMessages.UpdateRequest request) {
    val events = new LinkedList<Event>();
    this.name = request.getName();
    this.description = request.getDescription();
    val response = this
      .apply(new ItemCategoryMessages.SetParentRequest(request.getParent()));
    events.addAll(response.getEvents());
    events.add(new UpdatedEvent(this.id));
    return new ItemCategoryMessages.UpdateResponse(events);
  }

  public ItemCategoryMessages.DeleteResponse apply(ItemCategoryMessages.DeleteRequest request) {
    return new ItemCategoryMessages.DeleteResponse(
      Arrays.asList(new ItemCategoryEvents.DeletedEvent(this.id))
    );
  }

  public ItemCategoryMessages.SetParentResponse apply(
    ItemCategoryMessages.SetParentRequest request) {
    val oldParent = this.parent;
    val oldKey = this.key;
    val oldPath = this.path;
    this.parent = request.getParent();
    this.key = Optional.ofNullable(parent)
      .map(p -> p.getKey() + KEY_DELIMITER + getId().getValue())
      .orElse(getId().getValue().toString());
    this.path = Optional.ofNullable(parent)
      .map(p -> p.getPath() + PATH_DELIMITER + getName())
      .orElse(getName());
    val events = new HashSet<Event>();
    if (!key.equals(oldKey) || !path.equals(oldPath)) {
      val oldParentId = Optional.ofNullable(oldParent)
        .map(ItemCategory::getId)
        .orElse(null);
      val newParentId = Optional.ofNullable(parent)
        .map(ItemCategory::getId)
        .orElse(null);
      events.add(new ItemCategoryEvents.ParentChangedEvent(this.id, oldParentId, newParentId));
    }
    return new ItemCategoryMessages.SetParentResponse(events);
  }

  public ItemCategoryMessages.PrepareImportResponse apply(
    ItemCategoryMessages.PrepareImportRequest request) {
    apply(new ItemCategoryMessages.SetParentRequest(this.parent));
    return new ItemCategoryMessages.PrepareImportResponse(
      Collections.emptyList()
    );
  }

  public void removeItem(Item item) {
    itemCount = itemCount.subtract(BigDecimal.ONE);
  }

}
