package pico.erp.item.spec.type;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.reinert.jjschema.v1.JsonSchemaFactory;
import com.github.reinert.jjschema.v1.JsonSchemaV4Factory;
import java.io.Serializable;
import java.util.Optional;
import lombok.Getter;
import lombok.SneakyThrows;
import pico.erp.item.spec.variables.ItemSpecVariables;

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

  public ClassBasedItemSpecType(String id, Class<T> variablesType) {
    this.id = ItemSpecTypeId.from(id);
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
