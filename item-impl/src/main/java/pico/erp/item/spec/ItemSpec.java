package pico.erp.item.spec;


import java.math.BigDecimal;
import java.util.Arrays;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pico.erp.item.Item;
import pico.erp.item.spec.ItemSpecMessages.CreateResponse;
import pico.erp.item.spec.ItemSpecMessages.DeleteResponse;
import pico.erp.item.spec.ItemSpecMessages.UpdateResponse;

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

  @SuppressWarnings("unchecked")
  public CreateResponse apply(ItemSpecMessages.CreateRequest request) {
    id = request.getId();
    item = request.getItem();
    variables = item.createSpecVariables();
    if (variables.isValid()) {
      baseUnitCost = item.getSpecType().calculateUnitCost(item, variables);
    }
    summary = variables.getSummary();
    return new CreateResponse(
      Arrays.asList(new ItemSpecEvents.CreatedEvent(this.id))
    );
  }

  @SuppressWarnings("unchecked")
  public UpdateResponse apply(ItemSpecMessages.UpdateRequest request) {
    variables = request.getVariables();
    if (variables.isValid()) {
      baseUnitCost = item.getSpecType().calculateUnitCost(item, variables);
    }
    summary = variables.getSummary();
    return new UpdateResponse(
      Arrays.asList(new ItemSpecEvents.UpdatedEvent(this.id))
    );
  }

  public DeleteResponse apply(ItemSpecMessages.DeleteRequest request) {
    return new DeleteResponse(
      Arrays.asList(new ItemSpecEvents.UpdatedEvent(this.id))
    );
  }

}
