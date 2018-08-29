package pico.erp.item;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Id;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pico.erp.shared.data.Menu;
import pico.erp.shared.data.Role;

@RequiredArgsConstructor
public enum ROLE implements Role {

  ITEM_MANAGER(Stream.of(
    MENU.ITEM_CATEGORY_MANAGEMENT,
    MENU.ITEM_MANAGEMENT
  ).collect(Collectors.toSet())),

  ITEM_ACCESSOR(Stream.of(
    MENU.ITEM_CATEGORY_MANAGEMENT,
    MENU.ITEM_MANAGEMENT
  ).collect(Collectors.toSet()));

  @Id
  @Getter
  private final String id = name();

  @Transient
  @Getter
  @NonNull
  private Set<Menu> menus;
}
