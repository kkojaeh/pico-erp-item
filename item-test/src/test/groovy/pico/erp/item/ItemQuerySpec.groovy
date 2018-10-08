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
import pico.erp.item.category.ItemCategoryId
import pico.erp.item.category.ItemCategoryRequests
import pico.erp.item.category.ItemCategoryService
import pico.erp.shared.IntegrationConfiguration
import pico.erp.shared.data.UnitKind
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ItemQuerySpec extends Specification {

  def setup() {
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01"), name: "생산자재"))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01-1"), name: "조립자재", parentId: ItemCategoryId.from("01")))

    itemService.create(new ItemRequests.CreateRequest(id: ItemId.from("item"), name: "아이템", categoryId: ItemCategoryId.from("01-1"), customerId: CompanyId.from("CUST1"), unit: UnitKind.EA, type: ItemTypeKind.MATERIAL, baseUnitCost: 0))
    itemService.create(new ItemRequests.CreateRequest(id: ItemId.from("item2"), name: "아이템2", categoryId: ItemCategoryId.from("01"), customerId: CompanyId.from("CUST1"), unit: UnitKind.EA, type: ItemTypeKind.MATERIAL, baseUnitCost: 0))
    itemService.create(new ItemRequests.CreateRequest(id: ItemId.from("item3"), name: "아이템3", customerId: CompanyId.from("CUST1"), unit: UnitKind.EA, type: ItemTypeKind.MATERIAL, baseUnitCost: 0))
  }

  @Autowired
  ItemCategoryService itemCategoryService

  @Autowired
  ItemQuery itemQuery

  @Autowired
  ItemService itemService

  def "상위 카테고리를 지정하여 검색하면 하위 카테고리와 매핑된 아이템도 조회된다"() {
    expect:
    def page = itemQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                                    | pageable               || totalElements
    new ItemView.Filter(categoryId: ItemCategoryId.from("01-1")) | new PageRequest(0, 10) || 1
    new ItemView.Filter(categoryId: ItemCategoryId.from("01"))   | new PageRequest(0, 10) || 2
  }

  def "카테고리를 지정하지 않은 품목도 조회된다"() {
    expect:
    def page = itemQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                         | pageable               || totalElements
    new ItemView.Filter(name: "아이템3") | new PageRequest(0, 10) || 1
    new ItemView.Filter()             | new PageRequest(0, 10) || 8
  }

}
