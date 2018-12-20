package pico.erp.item

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.item.spec.type.ItemSpecTypeExceptions
import pico.erp.item.spec.type.ItemSpecTypeId
import pico.erp.item.spec.type.ItemSpecTypeService
import pico.erp.shared.IntegrationConfiguration
import spock.lang.Specification

@SpringBootTest(classes = [IntegrationConfiguration])
@Transactional
@Rollback
@ActiveProfiles("test")
@Configuration
@ComponentScan("pico.erp.config")
class ItemSpecTypeServiceSpec extends Specification {

  @Autowired
  ItemSpecTypeService itemSpecTypeService


  def id = ItemSpecTypeId.from("material-paper")

  def unknownId = ItemSpecTypeId.from("unknown")

  def "조회 - 아이디로 조회"() {
    when:
    def itemSpecType = itemSpecTypeService.get(id)

    println itemSpecType
    then:
    itemSpecType.id == id

  }

  def "조회 - 존재하지 않는 아이디로 조회"() {
    when:
    itemSpecTypeService.get(unknownId)

    then:
    thrown(ItemSpecTypeExceptions.NotFoundException)
  }

}
