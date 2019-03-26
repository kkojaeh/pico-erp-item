package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.company.CompanyId
import pico.erp.config.ItemConfiguration
import pico.erp.item.category.ItemCategoryId
import pico.erp.item.category.ItemCategoryRequests
import pico.erp.item.category.ItemCategoryService
import pico.erp.item.spec.ItemSpecId
import pico.erp.item.spec.ItemSpecRequests
import pico.erp.item.spec.ItemSpecService
import pico.erp.item.spec.type.ItemSpecTypeId
import pico.erp.item.spec.type.ItemSpecTypeQuery
import pico.erp.shared.TestParentApplication
import pico.erp.shared.data.UnitKind
import spock.lang.Specification

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemSpecServiceSpec extends Specification {

  @Autowired
  ItemCategoryService itemCategoryService

  @Autowired
  ItemQuery itemQuery

  @Autowired
  ItemService itemService

  @Autowired
  ItemSpecTypeQuery itemSpecTypeQuery

  @Autowired
  ItemSpecService itemSpecService

  def itemId = ItemId.from("SC_180")

  def itemSpecId = ItemSpecId.from("test")

  def setup() {
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01"), name: "생산자재"))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01-1"), name: "조립자재", parentId: ItemCategoryId.from("01")))
    itemService.create(
      new ItemRequests.CreateRequest(
        id: itemId,
        name: "원지 SC_180",
        categoryId: ItemCategoryId.from("01-1"),
        customerId: CompanyId.from("CUST1"),
        unit: UnitKind.SHEET,
        type: ItemTypeKind.MATERIAL,
        baseUnitCost: 3.164,
        specTypeId: ItemSpecTypeId.from("material-paper")
      )
    )

    itemSpecService.create(
      new ItemSpecRequests.CreateRequest(
        id: itemSpecId,
        itemId: itemId
      )
    )
  }

  def "스펙 생성 후 변수를 유효하게 입력하면 단가가 생성된다"() {
    when:
    def vars = itemSpecService.get(itemSpecId).getVariables()
    vars.grammage = 180
    vars.width = 545
    vars.height = 364
    vars.incisionCount = 1
    println vars
    itemSpecService.update(new ItemSpecRequests.UpdateRequest(id: itemSpecId, variables: vars))
    def spec = itemSpecService.get(itemSpecId)

    then:
    spec.baseUnitCost == 225.96
  }

  def "스펙 생성 후 변수를 수정하면 단가가 변경된다"() {
    when:
    def vars = itemSpecService.get(itemSpecId).getVariables()
    vars.grammage = 180
    vars.width = 545
    vars.height = 364
    vars.incisionCount = 1
    println vars
    itemSpecService.update(new ItemSpecRequests.UpdateRequest(id: itemSpecId, variables: vars))
    def firstUnitCost = itemSpecService.get(itemSpecId).baseUnitCost
    vars.grammage = 300
    vars.width = 555
    vars.height = 414
    vars.incisionCount = 2
    println vars
    itemSpecService.update(new ItemSpecRequests.UpdateRequest(id: itemSpecId, variables: vars))
    def secondUnitCost = itemSpecService.get(itemSpecId).baseUnitCost

    then:
    firstUnitCost == 225.96
    secondUnitCost == 218.10
  }


  def "lock 하게 되면 품목의 단가 변경에 영향을 받지 않는다"() {
    when:
    def before = itemSpecService.get(itemSpecId)
    itemSpecService.lock(
      new ItemSpecRequests.LockRequest(
        id: itemSpecId
      )
    )

    itemService.update(
      new ItemRequests.UpdateRequest(
        id: itemId,
        name: "원지 SC_180",
        categoryId: ItemCategoryId.from("01-1"),
        customerId: CompanyId.from("CUST1"),
        unit: UnitKind.SHEET,
        type: ItemTypeKind.MATERIAL,
        baseUnitCost: 3.264,
        specTypeId: ItemSpecTypeId.from("material-paper")
      )
    )

    def after = itemSpecService.get(itemSpecId)

    then:
    before.baseUnitCost == after.baseUnitCost
    before.locked == false
    after.locked == true
  }

  def "품목의 단가가 변경되면 영향을 받게 된다"() {
    when:
    def before = itemSpecService.get(itemSpecId)
    itemService.update(
      new ItemRequests.UpdateRequest(
        id: itemId,
        name: "원지 SC_180",
        categoryId: ItemCategoryId.from("01-1"),
        customerId: CompanyId.from("CUST1"),
        unit: UnitKind.SHEET,
        type: ItemTypeKind.MATERIAL,
        baseUnitCost: 3.264,
        specTypeId: ItemSpecTypeId.from("material-paper")
      )
    )

    def after = itemSpecService.get(itemSpecId)


    then:
    before.baseUnitCost != after.baseUnitCost
  }

}
