package pico.erp.item.spec;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pico.erp.item.Item;
import pico.erp.item.spec.ItemSpecMessages.DeleteResponse;

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

  boolean locked;

  @SuppressWarnings("unchecked")
  public ItemSpecMessages.CreateResponse apply(ItemSpecMessages.CreateRequest request) {
    id = request.getId();
    item = request.getItem();
    variables = item.createSpecVariables();
    if (variables.isValid()) {
      baseUnitCost = item.getSpecType().calculateUnitCost(item, variables);
    }
    summary = variables.getSummary();
    return new ItemSpecMessages.CreateResponse(
      Arrays.asList(new ItemSpecEvents.CreatedEvent(this.id))
    );
  }

  @SuppressWarnings("unchecked")
  public ItemSpecMessages.UpdateResponse apply(ItemSpecMessages.UpdateRequest request) {
    variables = request.getVariables();
    if (variables.isValid()) {
      baseUnitCost = item.getSpecType().calculateUnitCost(item, variables);
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
    if (variables.isValid()) {
      baseUnitCost = item.getSpecType().calculateUnitCost(item, variables);
    }
    return new ItemSpecMessages.RecalculateResponse(
      Collections.emptyList()
    );
  }

  public ItemSpecMessages.DeleteResponse apply(ItemSpecMessages.DeleteRequest request) {
    return new DeleteResponse(
      Arrays.asList(new ItemSpecEvents.UpdatedEvent(this.id))
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

}
