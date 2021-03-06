package pico.erp.item;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import kkojaeh.spring.boot.component.SpringBootComponentReadyEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import pico.erp.item.category.ItemCategoryRequests;
import pico.erp.item.category.ItemCategoryService;
import pico.erp.item.lot.ItemLotRequests;
import pico.erp.item.lot.ItemLotService;
import pico.erp.item.spec.ItemSpecRequests;
import pico.erp.item.spec.ItemSpecService;
import pico.erp.item.spec.variables.ItemSpecVariables;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile({"test-data"})
public class TestDataInitializer implements ApplicationListener<SpringBootComponentReadyEvent> {

  @Lazy
  @Autowired
  private ItemCategoryService itemCategoryService;

  @Lazy
  @Autowired
  private ItemService itemService;

  @Lazy
  @Autowired
  private ItemSpecService itemSpecService;

  @Lazy
  @Autowired
  private ItemLotService itemLotService;


  @Autowired
  private DataProperties dataProperties;

  @Override
  public void onApplicationEvent(SpringBootComponentReadyEvent event) {
    dataProperties.itemCategories.forEach(itemCategoryService::create);
    dataProperties.items.forEach(itemService::create);
    dataProperties.itemSpecs.forEach(itemSpecService::create);
    dataProperties.itemSpecVariables.forEach(variable -> variable.ready());
    dataProperties.itemSpecVariables.forEach(itemSpecService::update);
    dataProperties.itemLots.forEach(itemLotService::create);
    dataProperties.activeItems.forEach(itemService::activate);
  }

  @Data
  @Configuration
  @ConfigurationProperties("data")
  public static class DataProperties {

    List<ItemCategoryRequests.CreateRequest> itemCategories = new LinkedList<>();

    List<ItemRequests.CreateRequest> items = new LinkedList<>();

    List<ItemSpecRequests.CreateRequest> itemSpecs = new LinkedList<>();

    List<ItemSpecTypedUpdateRequest> itemSpecVariables = new LinkedList<>();

    List<ItemLotRequests.CreateRequest> itemLots = new LinkedList<>();

    List<ItemRequests.ActivateRequest> activeItems = new LinkedList<>();

  }

  @Data
  @EqualsAndHashCode(callSuper = false)
  public static class ItemSpecTypedUpdateRequest extends ItemSpecRequests.UpdateRequest {

    Class<? extends ItemSpecVariables> variableType;

    Map<String, Object> variableData;

    @SneakyThrows
    public void ready() {
      ItemSpecVariables variables = variableType.newInstance();
      BeanUtils.populate(variables, variableData);
      setVariables(variables);
    }

  }

}
