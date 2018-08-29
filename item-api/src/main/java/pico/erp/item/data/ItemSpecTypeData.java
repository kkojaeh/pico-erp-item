package pico.erp.item.data;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class ItemSpecTypeData {

  ItemSpecTypeId id;

  String name;

  String description;

}
