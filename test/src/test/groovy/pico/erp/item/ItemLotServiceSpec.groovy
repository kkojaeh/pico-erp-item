package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.lot.*
import pico.erp.item.spec.ItemSpecCode
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

import java.time.OffsetDateTime

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

  def itemId = ItemId.from("item-1")

  def lotCode = ItemLotCode.from("20181102")

  def specCode = ItemSpecCode.NOT_APPLICABLE

  def unknownLotCode = ItemLotCode.from("unknown")

  def id = ItemLotId.from("item-lot")

  def unknownId = ItemLotId.from("unknown")

  def key = ItemLotKey.from(itemId, specCode, lotCode)

  def unknownKey = ItemLotKey.from(itemId, specCode, unknownLotCode)

  def setup() {
    itemLotService.create(
      new ItemLotRequests.CreateRequest(
        id: id,
        itemId: itemId,
        specCode: specCode,
        lotCode: lotCode
      )
    )
  }

  def "존재 - 아이디로 확인"() {
    when:
    def exists = itemLotService.exists(id)

    then:
    exists == true
  }

  def "존재 - 코드로 확인"() {
    when:
    def exists = itemLotService.exists(key)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = itemLotService.exists(unknownId)

    then:
    exists == false
  }

  def "존재 - 존재하지 않는 코드로 확인"() {
    when:

    def exists = itemLotService.exists(unknownKey)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def itemLot = itemLotService.get(id)

    then:
    itemLot.id == id
    itemLot.itemId == itemId
    itemLot.specCode == specCode
    itemLot.lotCode == lotCode
  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    itemLotService.get(unknownId)

    then:
    thrown(ItemLotExceptions.NotFoundException)
  }


  def "조회 - 코드로 조회"() {
    when:
    def itemLot = itemLotService.get(key)

    then:
    itemLot.id == id
    itemLot.itemId == itemId
    itemLot.specCode == specCode
    itemLot.lotCode == lotCode
  }

  def "조회 - 존재하지 않는 코드로 조회"() {
    when:
    itemLotService.get(unknownKey)

    then:
    thrown(ItemLotExceptions.NotFoundException)
  }

  def "조회 - 복수의 아이디로 조회"() {
    when:
    def itemLotId2 = ItemLotId.from("item-lot2")
    itemLotService.create(new ItemLotRequests.CreateRequest(
      id: itemLotId2,
      itemId: itemId,
      specCode: specCode,
      lotCode: ItemLotCode.from("201811021")
    ))
    def itemLots = itemLotService.getAll(Arrays.asList(id, itemLotId2))

    then:
    itemLots.size() == 2
  }

  def "수정 - 수정 한다"() {
    when:
    def expirationDate = OffsetDateTime.now().plusDays(1)
    itemLotService.update(
      new ItemLotRequests.UpdateRequest(
        id: id,
        expirationDate: expirationDate
      )
    )
    def itemLot = itemLotService.get(key)

    then:
    itemLot.expirationDate == expirationDate
  }

  def "만료 - 만료 기간이 지나면 만료 된다"() {
    when:
    itemLotService.update(
      new ItemLotRequests.UpdateRequest(
        id: id,
        expirationDate: OffsetDateTime.now().plusDays(1)
      )
    )
    itemLotService.expire(
      new ItemLotRequests.ExpireRequest(
        fixedDate: OffsetDateTime.now().plusDays(1)
      )
    )
    def itemLot = itemLotService.get(key)

    then:
    itemLot.expired == true
  }

}
