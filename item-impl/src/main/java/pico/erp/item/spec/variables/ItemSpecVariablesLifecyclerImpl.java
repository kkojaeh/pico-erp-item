package pico.erp.item.spec.variables;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pico.erp.item.spec.type.ItemSpecTypeExceptions;
import pico.erp.item.spec.type.ItemSpecTypeId;
import pico.erp.item.spec.type.ItemSpecTypeRepository;

@Component
public class ItemSpecVariablesLifecyclerImpl implements ItemSpecVariablesLifecycler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Lazy
  @Autowired
  private ItemSpecTypeRepository itemSpecTypeRepository;

  {
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  @Override
  public ItemSpecVariables initialize(ItemSpecTypeId typeId) {
    return itemSpecTypeRepository.findBy(typeId)
      .orElseThrow(ItemSpecTypeExceptions.NotFoundException::new)
      .create();
  }

  @SneakyThrows
  @Override
  public ItemSpecVariables parse(ItemSpecTypeId typeId, String text) {
    val type = itemSpecTypeRepository.findBy(typeId)
      .orElseThrow(ItemSpecTypeExceptions.NotFoundException::new);

    return (ItemSpecVariables) objectMapper.readValue(text, type.getType());
  }

  @SneakyThrows
  @Override
  public String stringify(ItemSpecTypeId typeId, ItemSpecVariables variables) {
    if (variables == null) {
      return null;
    }
    return objectMapper.writeValueAsString(variables);
  }
}
