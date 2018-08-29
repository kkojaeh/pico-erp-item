package pico.erp.item.data;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemCategoryData implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  ItemCategoryId id;

  ItemCategoryCode code;

  String name;

  ItemCategoryId parentId;

  String key;

  String path;

  BigDecimal itemCount;

  String description;

}
