package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.company.CompanyApplication
import pico.erp.config.ItemConfiguration
import pico.erp.item.category.ItemCategoryCode
import pico.erp.item.category.ItemCategoryId
import pico.erp.item.category.ItemCategoryService
import pico.erp.item.category.ItemCategoryTransporter
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblings = [CompanyApplication])
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemCategoryTransporterSpec extends Specification {

  @Autowired
  ItemCategoryTransporter itemCategoryTransporter

  @Autowired
  ItemCategoryService itemCategoryService

  @Value("classpath:item-category-import-data.xlsx")
  Resource importData

  def "export"() {
    when:
    def inputStream = itemCategoryTransporter.exportExcel(
      new ItemCategoryTransporter.ExportRequest(
        empty: false
      )
    )

    then:
    inputStream.contentLength > 0
  }

  def "import - 덮어쓴다"() {
    when:
    itemCategoryTransporter.importExcel(
      new ItemCategoryTransporter.ImportRequest(
        inputStream: importData.getInputStream(),
        overwrite: true
      )
    )
    def previous = itemCategoryService.get(ItemCategoryId.from("379c0f89-7db0-3a7e-804f-65f52cce2e75"))
    def created = itemCategoryService.get(ItemCategoryId.from("5688a2b8-fa7d-43cc-9382-1be2f286c748"))
    then:
    previous.name == "인쇄2"
    previous.description == "인쇄 설명2"
    created.code == ItemCategoryCode.from("100")
    created.name == "테스트 데이터1"
    created.itemCount == 0
    created.description == "테스트 설명1"
  }

  def "import - 덮어쓰지 않는다"() {
    when:
    itemCategoryTransporter.importExcel(
      new ItemCategoryTransporter.ImportRequest(
        inputStream: importData.getInputStream(),
        overwrite: false
      )
    )
    def previous = itemCategoryService.get(ItemCategoryId.from("379c0f89-7db0-3a7e-804f-65f52cce2e75"))
    def created = itemCategoryService.get(ItemCategoryId.from("daac5ec8-6cae-4783-af32-456a0e6881ca"))
    then:
    previous.name == "인쇄"
    previous.description == "인쇄 설명"
    created.code == ItemCategoryCode.from("101")
    created.name == "테스트 데이터2"
    created.itemCount == 0
    created.description == "테스트 설명2"
  }
}
