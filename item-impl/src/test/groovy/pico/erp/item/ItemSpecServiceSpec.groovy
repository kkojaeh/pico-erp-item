package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.data.CompanyId
import pico.erp.item.data.*
import pico.erp.shared.IntegrationConfiguration
import pico.erp.shared.data.UnitKind
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
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

  def setup() {
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01"), name: "생산자재"))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("01-1"), name: "조립자재", parentId: ItemCategoryId.from("01")))
    itemService.create(
      new ItemRequests.CreateRequest(
        id: ItemId.from("SC_180"),
        name: "원지 SC_180",
        categoryId: ItemCategoryId.from("01-1"),
        customerId: CompanyId.from("CUST1"),
        unit: UnitKind.SHEET,
        type: ItemTypeKind.MATERIAL,
        baseUnitCost: 3.164,
        specTypeId: ItemSpecTypeId.from("pico.erp.config.item.spec.PaperItemSpecVariables")
      )
    )

    itemSpecService.create(
      new ItemSpecRequests.CreateRequest(
        id: ItemSpecId.from("test"),
        itemId: ItemId.from("SC_180")
      )
    )
  }

  def "스펙 생성 후 변수를 유효하게 입력하면 단가가 생성된다"() {
    when:
    def vars = itemSpecService.get(ItemSpecId.from("test")).getVariables()
    vars.grammage = 180
    vars.width = 545
    vars.height = 364
    vars.incisionCount = 1
    println vars
    itemSpecService.update(new ItemSpecRequests.UpdateRequest(id: ItemSpecId.from("test"), variables: vars))
    def spec = itemSpecService.get(ItemSpecId.from("test"))

    then:
    spec.baseUnitCost == 225.96
  }

  def "스펙 생성 후 변수를 수정하면 단가가 변경된다"() {
    when:
    def vars = itemSpecService.get(ItemSpecId.from("test")).getVariables()
    vars.grammage = 180
    vars.width = 545
    vars.height = 364
    vars.incisionCount = 1
    println vars
    itemSpecService.update(new ItemSpecRequests.UpdateRequest(id: ItemSpecId.from("test"), variables: vars))
    def firstUnitCost = itemSpecService.get(ItemSpecId.from("test")).baseUnitCost
    vars.grammage = 300
    vars.width = 555
    vars.height = 414
    vars.incisionCount = 2
    println vars
    itemSpecService.update(new ItemSpecRequests.UpdateRequest(id: ItemSpecId.from("test"), variables: vars))
    def secondUnitCost = itemSpecService.get(ItemSpecId.from("test")).baseUnitCost

    then:
    firstUnitCost == 225.96
    secondUnitCost == 218.10
  }

}
