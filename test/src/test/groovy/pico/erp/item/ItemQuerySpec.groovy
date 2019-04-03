package pico.erp.item

import kkojaeh.spring.boot.component.SpringBootTestComponent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import pico.erp.config.ItemConfiguration
import pico.erp.item.category.ItemCategoryId
import pico.erp.item.category.ItemCategoryService
import pico.erp.shared.ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier
import pico.erp.shared.TestParentApplication
import spock.lang.Specification

@SpringBootTest(classes = [ItemApplication, ItemConfiguration])
@SpringBootTestComponent(parent = TestParentApplication, siblingsSupplier = ComponentDefinitionServiceLoaderTestComponentSiblingsSupplier.class)
@Transactional
@Rollback
@ActiveProfiles("test")
class ItemQuerySpec extends Specification {

  @Autowired
  ItemCategoryService itemCategoryService

  @Autowired
  ItemQuery itemQuery

  @Autowired
  ItemService itemService

  def "상위 카테고리를 지정하여 검색하면 하위 카테고리와 매핑된 아이템도 조회된다"() {
    expect:
    def page = itemQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                                          | pageable               || totalElements
    new ItemView.Filter(categoryId: ItemCategoryId.from("category-5")) | new PageRequest(0, 10) || 4
    new ItemView.Filter(categoryId: ItemCategoryId.from("category-3")) | new PageRequest(0, 10) || 1
  }

  def "카테고리를 지정하지 않은 품목도 조회된다"() {
    expect:
    def page = itemQuery.retrieve(condition, pageable)
    page.totalElements == totalElements

    where:
    condition                                 | pageable               || totalElements
    new ItemView.Filter(name: "헤어케어 선물 세트박스") | new PageRequest(0, 10) || 2
    new ItemView.Filter()                     | new PageRequest(0, 10) || 19
  }

}
