package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
import pico.erp.item.category.ItemCategoryService
import pico.erp.item.lot.*
import pico.erp.shared.IntegrationConfiguration
import pico.erp.shared.data.UnitKind
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ItemLotQuerySpec extends Specification {

  static def itemId = ItemId.from("item")

  def setup() {
    itemService.create(new ItemRequests.CreateRequest(id: itemId, name: "아이템", customerId: CompanyId.from("CUST1"), unit: UnitKind.EA, type: ItemTypeKind.MATERIAL, baseUnitCost: 0))

    itemLotService.create(new ItemLotRequests.CreateRequest(
      id: ItemLotId.from("item-lot"),
      itemId: itemId,
      code: ItemLotCode.from("20181102")
    ))
    itemLotService.create(new ItemLotRequests.CreateRequest(
      id: ItemLotId.from("item-lot2"),
      itemId: itemId,
      code: ItemLotCode.from("20181103")
    ))
  }

  @Autowired
  ItemCategoryService itemCategoryService

  @Autowired
  ItemLotQuery itemLotQuery

  @Autowired
  ItemService itemService

  @Autowired
  ItemLotService itemLotService

  def "조건을 변경하여 검색"() {
    expect:
    def page = itemLotQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                              | pageable               || totalElements
    new ItemLotView.Filter(itemId: itemId, code: "02")     | new PageRequest(0, 10) || 1
    new ItemLotView.Filter(itemId: itemId, code: "03")     | new PageRequest(0, 10) || 1
    new ItemLotView.Filter(itemId: itemId, code: "201811") | new PageRequest(0, 10) || 2
    new ItemLotView.Filter(itemId: itemId, expired: true)  | new PageRequest(0, 10) || 0
    new ItemLotView.Filter(itemId: itemId, expired: false) | new PageRequest(0, 10) || 2
  }

}
