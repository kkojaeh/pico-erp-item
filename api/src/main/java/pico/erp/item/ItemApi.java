package pico.erp.item;

import javax.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.data.Role;

public final class ItemApi {

  public final static ApplicationId ID = ApplicationId.from("item");

  @RequiredArgsConstructor
  public enum Roles implements Role {

    ITEM_MANAGER,

    ITEM_ACCESSOR;

    @Id
    @Getter
    private final String id = name();

  }
}
