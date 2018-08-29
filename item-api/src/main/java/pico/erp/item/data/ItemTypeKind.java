package pico.erp.item.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pico.erp.shared.data.LocalizedDescriptable;
import pico.erp.shared.data.LocalizedNameable;

@AllArgsConstructor
public enum ItemTypeKind implements LocalizedNameable, LocalizedDescriptable {

  // http://12foot.tistory.com/3?category=729435

  /**
   * 상품
   *
   * 취득한 품목을 별도의 가공없이 그대로 판매하는 품목
   *
   * MERCHANDISE(true, true, false, false),
   */

  /**
   * 제품
   *
   * 기업에서 재료를 투입하여, 생산 또는 가공과정을 거쳐 판매하는 품목
   */
  PRODUCT(true, false, true, true),
  /**
   * 반제품
   *
   * 제품을 만들기 위한 중간 생산단계의 품목.  판매와 보관이 가능합니다.
   */
  SEMI_PRODUCT(true, true, true, true),
  /**
   * 재공품
   *
   * 제품 또는 반제품을 만들기 위해 생산 또는 가공한 중간 단계의 품목.  공정 중에 있는 품목을 지칭하며, 판매할 수 없다.
   */
  WORK_IN_PROCESS(false, true, true, true),

  /**
   * 원재료
   *
   * 제품 또는 반제품을 생산 또는 가공하기 위한 주된 재료 원재료도 판매가 가능합니다. 물론 직접적인 판매가 아니라, 상품으로 대체되어 판매되지요.
   */
  MATERIAL(true, true, false, true),
  /**
   * 저장품
   *
   * 제품 또는 반제품의 생산 또는 가공을 위해 저장해놓고 쓰는 재료. 나사못, 납땜용 인두, 볼트, 너트등 수선용 소모품등과 같이 미리 사두고 저장해놓고 사용하는 것들..
   * (소모품비 또는 소모공구비와 같은 형태로 비용화 하기도 합니다.)
   */
  SUPPLIES(false, true, false, false);

  /**
   * 판매 가능 여부
   */
  @Getter
  boolean sellable;

  /**
   * 투입 가능 여부
   */
  @Getter
  boolean materializable;

  /**
   * 공정 필요 여부
   */
  @Getter
  boolean processNeeded;

  @Getter
  boolean bomRelated;

  @Override
  public String getDescriptionCode() {
    return getClass().getName() + "." + name() + ".description";
  }

}
