package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.config.ItemConfiguration
import pico.erp.item.category.*
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

import java.util.concurrent.TimeUnit

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemCategoryServiceSpec extends Specification {

  @Autowired
  ItemCategoryService itemCategoryService

  def id = ItemCategoryId.from("test")

  def unknownId = ItemCategoryId.from("unknown")

  def unknownCode = ItemCategoryCode.from("unknown")

  def name = "생산자재"

  def setup() {
    itemCategoryService.create(
      new ItemCategoryRequests.CreateRequest(id: id, name: name)
    )
  }

  def "존재 - 아이디로 확인"() {
    when:
    def exists = itemCategoryService.exists(id)

    then:
    exists == true
  }

  def "존재 - 코드로 확인"() {
    when:
    def itemCategory = itemCategoryService.get(id)
    def exists = itemCategoryService.exists(itemCategory.code)

    then:
    exists == true
  }

  def "존재 - 존재하지 않는 아이디로 확인"() {
    when:
    def exists = itemCategoryService.exists(unknownId)

    then:
    exists == false
  }

  def "존재 - 존재하지 않는 코드로 확인"() {
    when:
    def exists = itemCategoryService.exists(unknownCode)

    then:
    exists == false
  }

  def "조회 - 아이디로 조회"() {
    when:
    def itemCategory = itemCategoryService.get(id)

    then:
    itemCategory.id == id
    itemCategory.name == "생산자재"
  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    itemCategoryService.get(unknownId)

    then:
    thrown(ItemCategoryExceptions.NotFoundException)
  }


  def "조회 - 코드로 조회"() {
    when:
    def itemCategory = itemCategoryService.get(id)
    itemCategory = itemCategoryService.get(itemCategory.code)

    then:
    itemCategory.id == id
    itemCategory.name == "생산자재"
  }

  def "조회 - 존재하지 않는 코드로 조회"() {
    when:
    itemCategoryService.get(unknownCode)

    then:
    thrown(ItemCategoryExceptions.NotFoundException)
  }

  def "수정 - 부모를 변경하면 하위 자식들의 key 와 path 가 같이 변경된다"() {
    when:
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1"), name: "일"))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1-1"), name: "일-일", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1-2"), name: "일-이", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1-1-1"), name: "일-일-일", parentId: ItemCategoryId.from("C1-1")))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C2"), name: "이"))
    itemCategoryService.update(new ItemCategoryRequests.UpdateRequest(id: ItemCategoryId.from("C1-1"), name: "일-일", parentId: ItemCategoryId.from("C2")))
    TimeUnit.SECONDS.sleep(3)
    def itemCategory = itemCategoryService.get(ItemCategoryId.from("C1-1-1"))

    then:
    itemCategory.path == "이 > 일-일 > 일-일-일"
  }

  def "부모의 이름을 변경하면 하위 자식들의 path 가 같이 변경된다"() {
    when:
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1"), name: "일"))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1-1"), name: "일-일", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1-2"), name: "일-이", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C1-1-1"), name: "일-일-일", parentId: ItemCategoryId.from("C1-1")))
    itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("C2"), name: "이"))
    itemCategoryService.update(new ItemCategoryRequests.UpdateRequest(id: ItemCategoryId.from("C1-1"), parentId: ItemCategoryId.from("C1"), name: "삼"))
    TimeUnit.SECONDS.sleep(3)
    def itemCategory = itemCategoryService.get(ItemCategoryId.from("C1-1-1"))

    then:
    itemCategory.path == "일 > 삼 > 일-일-일"
  }


}
