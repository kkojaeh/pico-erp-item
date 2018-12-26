package pico.erp.item.spec;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import pico.erp.item.Item;
import pico.erp.item.lot.ItemLotCode;
import pico.erp.item.spec.ItemSpecMessages.DeleteResponse;
import pico.erp.item.spec.variables.ItemSpecVariables;
import pico.erp.shared.data.UnitKind;
import pico.erp.shared.event.Event;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public class ItemSpec {

  @Id
  ItemSpecId id;

  Item item;

  ItemSpecVariables variables;

  String summary;

  BigDecimal baseUnitCost;

  BigDecimal purchaseUnitCost;

  boolean locked;

  @SuppressWarnings("unchecked")
  public ItemSpecMessages.CreateResponse apply(ItemSpecMessages.CreateRequest request) {
    id = request.getId();
    item = request.getItem();
    variables = request.getItemSpecVariablesLifecycler().initialize(item.getSpecTypeId());
    if (variables.isValid()) {
      baseUnitCost = variables.calculateUnitCost(item);
      purchaseUnitCost = variables.calculatePurchaseUnitCost(item);
    }
    summary = variables.getSummary();
    return new ItemSpecMessages.CreateResponse(
      Arrays.asList(new ItemSpecEvents.CreatedEvent(this.id))
    );
  }

  @SuppressWarnings("unchecked")
  public ItemSpecMessages.UpdateResponse apply(ItemSpecMessages.UpdateRequest request) {
    if (locked) {
      throw new ItemSpecExceptions.CannotRecalculateException();
    }
    variables = request.getVariables();
    if (variables.isValid()) {
      baseUnitCost = variables.calculateUnitCost(item);
      purchaseUnitCost = variables.calculatePurchaseUnitCost(item);
    }
    summary = variables.getSummary();
    return new ItemSpecMessages.UpdateResponse(
      Arrays.asList(new ItemSpecEvents.UpdatedEvent(this.id))
    );
  }

  @SuppressWarnings("unchecked")
  public ItemSpecMessages.RecalculateResponse apply(ItemSpecMessages.RecalculateRequest request) {
    if (locked) {
      throw new ItemSpecExceptions.CannotRecalculateException();
    }
    val events = new LinkedList<Event>();
    val oldBaseUnitCost = baseUnitCost;
    if (variables.isValid()) {
      baseUnitCost = variables.calculateUnitCost(item);
      purchaseUnitCost = variables.calculatePurchaseUnitCost(item);
    }
    if (!oldBaseUnitCost.equals(baseUnitCost)) {
      events.add(new ItemSpecEvents.UpdatedEvent(this.id));
    }
    return new ItemSpecMessages.RecalculateResponse(
      events
    );
  }

  public ItemSpecMessages.DeleteResponse apply(ItemSpecMessages.DeleteRequest request) {
    return new DeleteResponse(
      Arrays.asList(new ItemSpecEvents.DeletedEvent(this.id))
    );
  }

  public ItemSpecMessages.LockResponse apply(ItemSpecMessages.LockRequest request) {
    locked = true;
    return new ItemSpecMessages.LockResponse(
      Arrays.asList(new ItemSpecEvents.UpdatedEvent(this.id))
    );
  }

  public ItemSpecMessages.UnlockResponse apply(ItemSpecMessages.UnlockRequest request) {
    locked = false;
    return new ItemSpecMessages.UnlockResponse(
      Arrays.asList(new ItemSpecEvents.UpdatedEvent(this.id))
    );
  }

  public BigDecimal calculatePurchaseQuantity(BigDecimal quantity) {
    return variables.calculatePurchaseQuantity(quantity);
  }

  public UnitKind getPurchaseUnit() {
    return variables.getPurchaseUnit();
  }

  public UnitKind getUnit() {
    return variables.getUnit();
  }

  public ItemLotCode getLotCode() {
    return ItemLotCode.from(this.getSummary());
  }

}
