package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.config.ItemConfiguration
import pico.erp.item.lot.ItemLotQuery
import pico.erp.item.lot.ItemLotView
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemLotQuerySpec extends Specification {

  static def itemId = ItemId.from("item-1")

  @Autowired
  ItemLotQuery itemLotQuery

  def "조건을 변경하여 검색"() {
    expect:
    def page = itemLotQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                              | pageable               || totalElements
    new ItemLotView.Filter(itemId: itemId, code: "1018")   | new PageRequest(0, 10) || 1
    new ItemLotView.Filter(itemId: itemId, code: "1019")   | new PageRequest(0, 10) || 1
    new ItemLotView.Filter(itemId: itemId, code: "201810") | new PageRequest(0, 10) || 3
    new ItemLotView.Filter(itemId: itemId, expired: true)  | new PageRequest(0, 10) || 0
    new ItemLotView.Filter(itemId: itemId, expired: false) | new PageRequest(0, 10) || 3
  }

}
