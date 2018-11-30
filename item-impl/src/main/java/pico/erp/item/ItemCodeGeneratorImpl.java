package pico.erp.item;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemCodeGeneratorImpl implements ItemCodeGenerator {

  @Autowired
  private ItemRepository itemRepository;

  @Override
  public ItemCode generate(Item item) {
    val now = OffsetDateTime.now();
    val begin = now.with(LocalTime.MIN);
    val end = now.with(LocalTime.MAX);
    val date =
      Integer.toString(now.getYear() - 1900, 36) + Integer.toString(now.getMonthValue(), 16)
        + Integer.toString(now.getDayOfMonth(), 36);
    val count = itemRepository.countCreatedBetween(begin, end);
    val code = String.format("%s-%03d", date, count + 1).toUpperCase();
    return ItemCode.from(code);
  }

}
