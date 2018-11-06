package pico.erp.item;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ItemCodeGeneratorImpl implements ItemCodeGenerator {

  @Autowired
  private ItemRepository itemRepository;

  @Value("${item.category.undefined-code}")
  private String undefinedCompanyCode = "000";

  /**
   * yyM
   */
  @Override
  public ItemCode generate(Item item) {
    val customerPart = Optional.ofNullable(item.getCustomer())
      .map(customer -> customer.getId().getValue())
      .orElse(undefinedCompanyCode);
    val previousCode = item.getCode();
    if (previousCode != null) {
      val split = StringUtils.split(previousCode.getValue(), "-");
      split[0] = customerPart;
      return ItemCode.from(StringUtils.arrayToDelimitedString(split, "-"));
    }
    val beginMonth = OffsetDateTime.now().with(TemporalAdjusters.firstDayOfMonth())
      .with(LocalTime.MIN);
    val endMonth = OffsetDateTime.now().with(TemporalAdjusters.lastDayOfMonth())
      .with(LocalTime.MAX);
    val now = LocalDate.now();
    val monthPart = (now.getYear() % 100) * 100 + Integer.toString(now.getMonthValue(), 16);
    val countPart = itemRepository.countCreatedBetween(beginMonth, endMonth);
    val code = String.format("%s-%s-%03d", customerPart, monthPart, countPart).toUpperCase();
    return ItemCode.from(code);
  }

}
