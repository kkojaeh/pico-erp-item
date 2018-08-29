package pico.erp.item.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.reinert.jjschema.v1.JsonSchemaFactory;
import com.github.reinert.jjschema.v1.JsonSchemaV4Factory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiFunction;
import lombok.Getter;
import lombok.SneakyThrows;
import pico.erp.item.data.ItemInfo;
import pico.erp.item.data.ItemSpecType;
import pico.erp.item.data.ItemSpecTypeId;
import pico.erp.item.data.ItemSpecVariables;

public class ClassBasedItemSpecType<T extends ItemSpecVariables> implements
  ItemSpecType<T> {

  private final static JsonSchemaFactory schemaFactory = new JsonSchemaV4Factory();

  static {
    schemaFactory.setAutoPutDollarSchema(true);
  }

  private final Class<T> variablesType;

  @Getter
  private final ItemSpecTypeId id;

  @Getter
  private final String name;

  @Getter
  private final String description;

  @Getter
  private final Serializable metadata;

  private final BiFunction<ItemInfo, T, BigDecimal> unitCostCalculator;

  public ClassBasedItemSpecType(Class<T> variablesType,
    BiFunction<ItemInfo, T, BigDecimal> unitCostCalculator) {
    this.unitCostCalculator = unitCostCalculator;
    this.id = ItemSpecTypeId.from(variablesType.getName());
    this.variablesType = variablesType;
    JsonNode schema = schemaFactory.createSchema(variablesType);
    TextNode titleNode = (TextNode) schema.get("title");
    TextNode descriptionNode = (TextNode) schema.get("description");
    this.name = Optional.ofNullable(titleNode)
      .map(node -> node.asText())
      .orElse("N/A");
    this.description = Optional.ofNullable(descriptionNode)
      .map(node -> node.asText())
      .orElse("N/A");
    this.metadata = schema.toString();
  }

  @Override
  public BigDecimal calculateUnitCost(ItemInfo item, T variables) {
    return unitCostCalculator.apply(item, variables);
  }

  @SneakyThrows
  @Override
  public T create() {
    return variablesType.newInstance();
  }

  @Override
  public Class<T> getType() {
    return variablesType;
  }

}
