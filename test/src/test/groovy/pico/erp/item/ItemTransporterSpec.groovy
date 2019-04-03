package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.Resource
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.config.ItemConfiguration
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import pico.erp.shared.data.UnitKind
import spock.lang.Specification

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemTransporterSpec extends Specification {

  @Autowired
  ItemTransporter itemTransporter

  @Autowired
  ItemService itemService

  @Value("classpath:item-import-data.xlsx")
  Resource importData

  def "export"() {
    when:
    def inputStream = itemTransporter.exportExcel(
      new ItemTransporter.ExportRequest(
        empty: false
      )
    )

    then:
    inputStream.contentLength > 0
  }

  def "import - 덮어쓴다"() {
    when:
    itemTransporter.importExcel(
      new ItemTransporter.ImportRequest(
        inputStream: importData.getInputStream(),
        overwrite: true
      )
    )
    def previous = itemService.get(ItemId.from("05e4075a-3f97-3d7c-b0bd-40329caa2423"))
    def created = itemService.get(ItemId.from("b1ff6cf3-df81-45ee-8aa5-0b4c0c1d1770"))
    then:
    previous.name == "HG105-0311 - 카드 - 인쇄완료2"
    previous.baseUnitCost == 80
    created.code == ItemCode.from("0000-001")
    created.name == "테스트 품목"
    created.externalCode == "AAAA"
    created.baseUnitCost == 0
    created.unit == UnitKind.EA
    created.type == ItemTypeKind.WORK_IN_PROCESS
    created.status == ItemStatusKind.ACTIVATED
  }

  def "import - 덮어쓰지 않는다"() {
    when:
    itemTransporter.importExcel(
      new ItemTransporter.ImportRequest(
        inputStream: importData.getInputStream(),
        overwrite: false
      )
    )
    def previous = itemService.get(ItemId.from("05e4075a-3f97-3d7c-b0bd-40329caa2423"))
    then:
    previous.name != "HG105-0311 - 카드 - 인쇄완료2"
    previous.baseUnitCost != 80
  }
}
