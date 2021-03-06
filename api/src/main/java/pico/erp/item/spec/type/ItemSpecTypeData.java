package pico.erp.item.spec.type;


import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class ItemSpecTypeData {

  ItemSpecTypeId id;

  String name;

  String description;

  Serializable metadata;

}
