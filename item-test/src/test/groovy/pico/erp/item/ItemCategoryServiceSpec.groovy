package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.category.*
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ItemCategoryServiceSpec extends Specification {

  @Autowired
  ItemCategoryService itemCategoryService

  def itemCategory

  def setup() {
    itemCategory = itemCategoryService.create(new ItemCategoryRequests.CreateRequest(id: ItemCategoryId.from("ACE"), name: "생산자재"))
  }

  def "아이디로 존재하는 품목 분류 확인"() {
    when:
    def exists = itemCategoryService.exists(ItemCategoryId.from("ACE"))

    then:
    exists == true
  }

  def "아이디로 존재하지 않는 품목 분류 확인"() {
    when:
    def exists = itemCategoryService.exists(ItemCategoryId.from("!ACE"))

    then:
    exists == false
  }

  def "아이디로 존재하는 품목 분류를 조회"() {
    when:
    def itemCategory = itemCategoryService.get(ItemCategoryId.from("ACE"))

    then:
    itemCategory.id == ItemCategoryId.from("ACE")
    itemCategory.name == "생산자재"
  }

  def "아이디로 존재하지 않는 품목 분류를 조회"() {
    when:
    itemCategoryService.get(ItemCategoryId.from("!ACE"))

    then:
    thrown(ItemCategoryExceptions.NotFoundException)
  }


  def "코드로 존재하는 품목 분류 확인"() {
    when:
    def exists = itemCategoryService.exists(itemCategory.code)

    then:
    exists == true
  }

  def "코드로 존재하지 않는 품목 분류 확인"() {
    when:
    def exists = itemCategoryService.exists(ItemCategoryCode.from("unknown"))

    then:
    exists == false
  }

  def "코드로 존재하는 품목 분류를 조회"() {
    when:
    def itemCategory = itemCategoryService.get(itemCategory.code)

    then:
    itemCategory.id == ItemCategoryId.from("ACE")
    itemCategory.name == "생산자재"
  }

  def "코드로 존재하지 않는 품목 분류를 조회"() {
    when:
    itemCategoryService.get(ItemCategoryCode.from("unknown"))

    then:
    thrown(ItemCategoryExceptions.NotFoundException)
  }
/*
  def "부모를 변경하면 하위 자식들의 key 와 path 가 같이 변경된다"() {
    when:
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1"), name: "일"))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1-1"), name: "일-일", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1-2"), name: "일-이", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1-1-1"), name: "일-일-일", parentId: ItemCategoryId.from("C1-1")))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C2"), name: "이"))
    itemCategoryService.update(new ItemCategoryService.UpdateRequest(id: ItemCategoryId.from("C1-1"), name: "일-일", parentId: ItemCategoryId.from("C2")))
    TimeUnit.SECONDS.sleep(3)
    def itemCategory = itemCategoryService.get(ItemCategoryId.from("C1-1-1"))

    then:
    itemCategory.path == "이 / 일-일 / 일-일-일"
    itemCategory.key == "C2/C1-1/C1-1-1"
  }

  def "부모의 이름을 변경하면 하위 자식들의 path 가 같이 변경된다"() {
    when:
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1"), name: "일"))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1-1"), name: "일-일", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1-2"), name: "일-이", parentId: ItemCategoryId.from("C1")))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C1-1-1"), name: "일-일-일", parentId: ItemCategoryId.from("C1-1")))
    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("C2"), name: "이"))
    itemCategoryService.update(new ItemCategoryService.UpdateRequest(id: ItemCategoryId.from("C1-1"), parentId: ItemCategoryId.from("C1"), name: "삼"))
    TimeUnit.SECONDS.sleep(3)
    def itemCategory = itemCategoryService.get(ItemCategoryId.from("C1-1-1"))

    then:
    itemCategory.path == "일 / 삼 / 일-일-일"
    itemCategory.key == "C1/C1-1/C1-1-1"
  }*/


}
