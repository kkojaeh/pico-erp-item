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

  def itemId = ItemId.from("item")

  def unknownItemId = ItemId.from("unknown")

  def unknownItemCode = ItemCode.from("unknown")

  def categoryId = ItemCategoryId.from("category-1")

  def customerId = CompanyId.from("CUST1")

  def barcodeNumber = "8809451795679"

  def name = "아이템"

  def setup() {
    itemService.create(
      new ItemRequests.CreateRequest(
        id: itemId,
        name: name,
        categoryId: categoryId,
        customerId: customerId,
        unit: UnitKind.EA,
        type: ItemTypeKind.MATERIAL,
        baseUnitCost: 0,
        barcodeNumber: barcodeNumber
      )
    )
  }

  def "존재 - 아이디로 확인"() {
    when:
    def exists = itemService.exists(itemId)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = itemService.exists(unknownItemId)

    then:
    exists == false
  }

  def "존재 - 코드로 확인"() {
    when:
    def code = itemService.get(itemId).code
    def exists = itemService.exists(code)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 코드로 확인"() {
    when:

    def exists = itemService.exists(unknownItemCode)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def item = itemService.get(itemId)

    then:
    item.id == itemId
    item.name == name
    item.categoryId == categoryId
    item.customerId == customerId
    item.unit == UnitKind.EA
    item.type == ItemTypeKind.MATERIAL
    item.baseUnitCost == 0
    item.barcodeNumber == barcodeNumber
  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    itemService.get(unknownItemId)

    then:
    thrown(ItemExceptions.NotFoundException)
  }


  def "조회 - 코드로 조회"() {
    when:
    def code = itemService.get(itemId).code
    def item = itemService.get(code)

    then:
    item.id == itemId
    item.name == "아이템"
  }

  def "조회 - 존재하지 않는 코드로 조회"() {
    when:
    itemService.get(unknownItemCode)

    then:
    thrown(ItemExceptions.NotFoundException)
  }


}
