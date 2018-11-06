package pico.erp.item.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemCategoryCodeGeneratorImpl implements ItemCategoryCodeGenerator {

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

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
}
