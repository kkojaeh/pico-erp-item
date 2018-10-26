package pico.erp.item.code;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pico.erp.item.Item;
import pico.erp.item.ItemCode;
import pico.erp.item.ItemRepository;
import pico.erp.item.category.ItemCategory;
import pico.erp.item.category.ItemCategoryCode;
import pico.erp.item.category.ItemCategoryRepository;
import pico.erp.item.lot.ItemLot;
import pico.erp.item.lot.ItemLotCode;

@Component
public class ItemCodeGeneratorImpl implements ItemCodeGenerator {

  private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMM");

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Value("${item.category.undefined-code}")
  private String undefinedCompanyCode = "000";

  @Override
  public ItemCategoryCode generate(ItemCategory itemCategory) {
    int count = (int) itemCategoryRepository.countAll() + 1;
    String formatted = String.format("%3s", Integer.toString(count, 36));
    if (formatted.length() > 3) {
      throw new RuntimeException("limit exceeded");
    }
    String replaced = formatted.replace(' ', '0');
    return ItemCategoryCode.from(replaced.toUpperCase());
  }

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
    val now = LocalDate.now();
    val monthPart = (now.getYear() % 100) * 100 + Integer.toString(now.getMonthValue(), 16);
    val countPart = itemRepository.countByCreatedThisMonth();
    val code = String.format("%s-%s-%03d", customerPart, monthPart, countPart).toUpperCase();
    return ItemCode.from(code);
  }

  @Override
  public ItemLotCode generate(ItemLot item) {
    val code = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    return ItemLotCode.from(code);
  }

}
