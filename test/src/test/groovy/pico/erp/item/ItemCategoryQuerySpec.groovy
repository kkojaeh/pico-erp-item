package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.config.ItemConfiguration
import pico.erp.item.category.ItemCategoryQuery
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemCategoryQuerySpec extends Specification {

  @Autowired
  ItemCategoryQuery itemCategoryQuery

  def "카테고리 조회"() {
    when:
    def list = itemCategoryQuery.findAllAsHierarchy()
    then:
    println list
    list.size() == 5
  }

}
