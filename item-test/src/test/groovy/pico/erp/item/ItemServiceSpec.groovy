package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
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
class ItemServiceSpec extends Specification {

  @Autowired
  ItemCategoryService itemCategoryService

  @Autowired
  ItemQuery itemQuery

  @Autowired
  ItemService itemService

  def item

  def setup() {
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01"), name: "생산자재"))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01-1"), name: "조립자재", parentId: ItemCategoryId.from("01")))
    item = itemService.create(new ItemRequests.CreateRequest(id: ItemId.from("item"), name: "아이템", categoryId: ItemCategoryId.from("01-1"), customerId: CompanyId.from("CUST1"), unit: UnitKind.EA, type: ItemTypeKind.MATERIAL, baseUnitCost: 0))
  }

  def "아이디로 존재하는 품목 확인"() {
    when:
    def exists = itemService.exists(ItemId.from("item"))

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 품목 확인"() {
    when:
    def exists = itemService.exists(ItemId.from("!item"))

    then:
    exists == false
  }

  def "아이디로 존재하는 품목를 조회"() {
    when:
    def item = itemService.get(ItemId.from("item"))

    then:
    item.id.value == "item"
    item.name == "아이템"
  }

  def "아이디로 존재하지 않는 품목를 조회"() {
    when:
    itemService.get(ItemId.from("!item"))

    then:
    thrown(ItemExceptions.NotFoundException)
  }


  def "코드로 존재하는 품목 확인"() {
    when:
    def exists = itemService.exists(item.code)

    then:
    exists == true
  }

  def "코드로 존재하지 않는 품목 확인"() {
    when:

    def exists = itemService.exists(ItemCode.from("unknown"))

    then:
    exists == false
  }

  def "코드로 존재하는 품목를 조회"() {
    when:
    def item = itemService.get(item.code)

    then:
    item.id.value == "item"
    item.name == "아이템"
  }

  def "코드로 존재하지 않는 품목를 조회"() {
    when:
    itemService.get(ItemCode.from("unknown"))

    then:
    thrown(ItemExceptions.NotFoundException)
  }


}
