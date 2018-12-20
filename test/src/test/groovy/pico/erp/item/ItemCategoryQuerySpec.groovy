package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.category.ItemCategoryQuery
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
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
