package pico.erp.item;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Role;

public final class ItemApi {

  @RequiredArgsConstructor
  public enum Roles implements Role {

    ITEM_MANAGER,

    ITEM_ACCESSOR;

    @Id
    @Getter
    private final String id = name();

  }
}
