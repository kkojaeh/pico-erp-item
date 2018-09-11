package pico.erp.item.category;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
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
import pico.erp.item.category.ItemCategoryEvents.CreatedEvent;
import pico.erp.item.category.ItemCategoryEvents.DeletedEvent;
import pico.erp.item.category.ItemCategoryEvents.ParentChangedEvent;
import pico.erp.item.category.ItemCategoryEvents.UpdatedEvent;
import pico.erp.item.category.ItemCategoryMessages.CreateResponse;
import pico.erp.item.category.ItemCategoryMessages.DeleteResponse;
import pico.erp.item.category.ItemCategoryMessages.SetParentResponse;
import pico.erp.item.category.ItemCategoryMessages.UpdateResponse;
import pico.erp.item.data.ItemCategoryCode;
import pico.erp.item.data.ItemCategoryId;
import pico.erp.item.domain.Item;
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

  public CreateResponse apply(ItemCategoryMessages.CreateRequest request) {
    this.id = request.getId();
    this.name = request.getName();
    this.description = request.getDescription();
    this.parent = request.getParent();
    this.apply(new ItemCategoryMessages.SetParentRequest(request.getParent()));
    this.code = request.getItemCodeGenerator().generate(this);
    return new CreateResponse(
      Arrays.asList(new CreatedEvent(this.id))
    );
  }

  public UpdateResponse apply(ItemCategoryMessages.UpdateRequest request) {
    val events = new LinkedList<Event>();
    this.name = request.getName();
    this.description = request.getDescription();
    val setParentResponse = this
      .apply(new ItemCategoryMessages.SetParentRequest(request.getParent()));
    events.addAll(setParentResponse.getEvents());
    events.add(new UpdatedEvent(this.id));
    return new UpdateResponse(events);
  }

  public DeleteResponse apply(ItemCategoryMessages.DeleteRequest request) {
    return new DeleteResponse(
      Arrays.asList(new DeletedEvent(this.id))
    );
  }

  public SetParentResponse apply(
    ItemCategoryMessages.SetParentRequest request) {
    val oldParent = this.parent;
    val oldKey = this.key;
    val oldPath = this.path;
    key = Optional.ofNullable(parent)
      .map(p -> p.getKey() + KEY_DELIMITER + getId().getValue())
      .orElse(getId().getValue());
    path = Optional.ofNullable(parent)
      .map(p -> p.getPath() + PATH_DELIMITER + getName())
      .orElse(getName());
    this.parent = request.getParent();
    val events = new HashSet<Event>();
    if (!key.equals(oldKey) || !path.equals(oldPath)) {
      val oldParentId = Optional.ofNullable(oldParent)
        .map(ItemCategory::getId)
        .orElse(null);
      val newParentId = Optional.ofNullable(parent)
        .map(ItemCategory::getId)
        .orElse(null);
      events.add(new ParentChangedEvent(this.id, oldParentId, newParentId));
    }
    return new SetParentResponse(events);
  }

  public void removeItem(Item item) {
    itemCount = itemCount.subtract(BigDecimal.ONE);
  }

}
