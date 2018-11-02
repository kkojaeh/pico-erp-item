package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyId
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
class ItemLotServiceSpec extends Specification {


  @Autowired
  ItemQuery itemQuery

  @Autowired
  ItemService itemService

  @Autowired
  ItemLotService itemLotService

  def itemId = ItemId.from("item")

  def unknownItemId = ItemId.from("unknown")

  def itemLotCode = ItemLotCode.from("20181102")

  def unknownItemLotCode = ItemLotCode.from("unknown")

  def itemLotId = ItemLotId.from("item-lot")

  def unknownItemLotId = ItemLotId.from("unknown")

  def item

  def setup() {
    itemService.create(new ItemRequests.CreateRequest(id: itemId, name: "아이템", customerId: CompanyId.from("CUST1"), unit: UnitKind.EA, type: ItemTypeKind.MATERIAL, baseUnitCost: 0))

    itemLotService.create(new ItemLotRequests.CreateRequest(
      id: itemLotId,
      itemId: itemId,
      code: itemLotCode
    ))
  }

  def "아이디로 존재하는 품목LOT 확인"() {
    when:
    def exists = itemLotService.exists(itemLotId)

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 품목LOT 확인"() {
    when:
    def exists = itemLotService.exists(unknownItemLotId)

    then:
    exists == false
  }

  def "아이디로 존재하는 품목LOT를 조회"() {
    when:
    def itemLot = itemLotService.get(itemLotId)

    then:
    itemLot.id == itemLotId
    itemLot.itemId == itemId
  }

  def "아이디로 존재하지 않는 품목LOT를 조회"() {
    when:
    itemLotService.get(unknownItemLotId)

    then:
    thrown(ItemLotExceptions.NotFoundException)
  }


  def "코드로 존재하는 품목LOT 확인"() {
    when:
    def exists = itemLotService.exists(itemId, itemLotCode)

    then:
    exists == true
  }

  def "코드로 존재하지 않는 품목LOT 확인"() {
    when:

    def exists = itemLotService.exists(itemId, unknownItemLotCode)

    then:
    exists == false
  }

  def "코드로 존재하는 품목LOT를 조회"() {
    when:
    def itemLot = itemLotService.get(itemId, itemLotCode)

    then:
    itemLot.id == itemLotId
    itemLot.itemId == itemId
  }

  def "코드로 존재하지 않는 품목LOT를 조회"() {
    when:
    itemLotService.get(itemId, unknownItemLotCode)

    then:
    thrown(ItemLotExceptions.NotFoundException)
  }

  def "복수의 아이디로 조회"() {
    when:
    def itemLotId2 = ItemLotId.from("item-lot2")
    itemLotService.create(new ItemLotRequests.CreateRequest(
      id: itemLotId2,
      itemId: itemId,
      code: ItemLotCode.from("201811021")
    ))
    def itemLots = itemLotService.getAll(Arrays.asList(itemLotId, itemLotId2))

    then:
    itemLots.size() == 2
  }


}
