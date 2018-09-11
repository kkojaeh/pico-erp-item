package pico.erp.item;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

@RequiredArgsConstructor
public enum ROLE implements Role {

  ITEM_MANAGER,

  ITEM_ACCESSOR;

  @Id
  @Getter
  private final String id = name();

}
