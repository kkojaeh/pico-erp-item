package pico.erp.config;

import kkojaeh.spring.boot.component.Give;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pico.erp.config.item.spec.CorrugatedFiberBoardItemSpecVariables;
import pico.erp.config.item.spec.MaterialPaperItemSpecVariables;
import pico.erp.config.item.spec.PetItemSpecVariables;
import pico.erp.item.spec.type.ClassBasedItemSpecType;
import pico.erp.item.spec.type.ItemSpecType;

@Configuration
public class ItemConfiguration {

  @Give
  @Bean
  public ItemSpecType<CorrugatedFiberBoardItemSpecVariables> corrugatedFiberBoardItemSpecType() {
    return new ClassBasedItemSpecType<>("corrugated-fiber-board",
      CorrugatedFiberBoardItemSpecVariables.class);
  }

  @Give
  @Bean
  public ItemSpecType<MaterialPaperItemSpecVariables> materialPaperItemSpecType() {
    return new ClassBasedItemSpecType<>("material-paper", MaterialPaperItemSpecVariables.class);
  }

  @Give
  @Bean
  public ItemSpecType<PetItemSpecVariables> petItemSpecType() {
    return new ClassBasedItemSpecType<>("pet", PetItemSpecVariables.class);
  }

}
