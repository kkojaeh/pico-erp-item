package pico.erp.item;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import pico.erp.item.data.ItemSpecVariables;
import pico.erp.shared.ApplicationInitializer;

@Configuration
@Profile({"!development", "!production"})
public class TestDataInitializer implements ApplicationInitializer {

  @Lazy
  @Autowired
  private ItemCategoryService itemCategoryService;

  @Lazy
  @Autowired
  private ItemService itemService;

  @Lazy
  @Autowired
  private ItemSpecService itemSpecService;


  @Autowired
  private DataProperties dataProperties;

  @Override
  public void initialize() {
    dataProperties.itemCategories.forEach(itemCategoryService::create);
    dataProperties.items.forEach(itemService::create);
    dataProperties.itemSpecs.forEach(itemSpecService::create);
    dataProperties.itemSpecVariables.forEach(variable -> variable.ready());
    dataProperties.itemSpecVariables.forEach(itemSpecService::update);
  }

  @Data
  @Configuration
  @ConfigurationProperties("data")
  public static class DataProperties {

    List<ItemCategoryRequests.CreateRequest> itemCategories = new LinkedList<>();

    List<ItemRequests.CreateRequest> items = new LinkedList<>();

    List<ItemSpecRequests.CreateRequest> itemSpecs = new LinkedList<>();

    List<ItemSpecTypedUpdateRequest> itemSpecVariables = new LinkedList<>();

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
      super.variables = variables;
    }

  }

}
