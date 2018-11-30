package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.lot.ItemLotQuery
import pico.erp.item.lot.ItemLotView
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
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
