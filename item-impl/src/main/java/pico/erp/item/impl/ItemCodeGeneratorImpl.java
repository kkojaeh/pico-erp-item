package pico.erp.item.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pico.erp.item.core.ItemCategoryRepository;
import pico.erp.item.core.ItemCodeGenerator;
import pico.erp.item.core.ItemRepository;
import pico.erp.item.data.ItemCategoryCode;
import pico.erp.item.data.ItemCode;
import pico.erp.item.data.ItemLotCode;
import pico.erp.item.domain.Item;
import pico.erp.item.domain.ItemCategory;
import pico.erp.item.domain.ItemLot;

@Component
public class ItemCodeGeneratorImpl implements ItemCodeGenerator {

  private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyMM");

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Value("${item.category.undefined-code}")
  private String undefinedCategoryCode = "000";

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
    StringBuilder builder = new StringBuilder();
    builder.append(
      Optional.ofNullable(item.getCategory())
        .map(category -> category.getCode().getValue())
        .orElse(undefinedCategoryCode)
    );
    ItemCode previousCode = item.getCode();
    if (previousCode != null) {
      builder.append(previousCode.getValue().substring(3));
      return ItemCode.from(builder.toString());
    }

    LocalDate now = LocalDate.now();
    builder.append('-');
    builder.append(now.getYear() % 100);
    builder.append(Integer.toString(now.getMonthValue(), 16));
    builder.append('-');

    long count = itemRepository.countByCreatedThisMonth();
    builder.append(String.format("%03d", count));
    return ItemCode.from(builder.toString());
  }

  @Override
  public ItemLotCode generate(ItemLot item) {
    return null;
  }

}
