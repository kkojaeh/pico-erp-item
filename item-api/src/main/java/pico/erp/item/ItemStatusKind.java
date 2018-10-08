package pico.erp.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.LocalizedNameable;

@AllArgsConstructor
public enum ItemStatusKind implements LocalizedNameable {

  /**
   * 작성중
   */
  DRAFT(true, false),

  /**
   * 사용중
   */
  ACTIVATED(false, true),

  /**
   * 비활성화
   */
  DEACTIVATED(true, false);

  @Getter
  private final boolean activatable;

  @Getter
  private final boolean deactivatable;

}
