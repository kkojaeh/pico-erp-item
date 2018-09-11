package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.category.ItemCategoryQuery
import pico.erp.item.category.ItemCategoryService
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ItemCategoryQuerySpec extends Specification {

  def setup() {
//    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("01"), name: "생산자재"))
//    itemCategoryService.create(new ItemCategoryService.CreateRequest(id: ItemCategoryId.from("01-1"), name: "조립자재", parentId: ItemCategoryId.from("01")))
  }

  @Autowired
  ItemCategoryQuery itemCategoryQuery

  @Autowired
  ItemCategoryService itemCategoryService

  def "카테고리 조회"() {
    when:
    def list = itemCategoryQuery.findAllAsHierarchy()
    then:
    println list
    list.size() == 5
  }

}
