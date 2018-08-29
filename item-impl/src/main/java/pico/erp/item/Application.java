package pico.erp.item;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.attachment.data.AttachmentCategory;
import pico.erp.attachment.data.AttachmentCategoryId;
import pico.erp.attachment.impl.AttachmentCategoryImpl;
import pico.erp.audit.data.AuditConfiguration;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;

@Slf4j
@SpringBootConfigs
public class Application implements ApplicationStarter {

  public static final String CONFIG_NAME = "item/application";

  public static final String CONFIG_NAME_PROPERTY = "spring.config.name=item/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(Application.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  public static void main(String[] args) {
    application().run(args);
  }

  @Public
  @Bean
  public AttachmentCategory attachmentCategory() {
    return new AttachmentCategoryImpl(AttachmentCategoryId.from("item"), "품목");
  }

  @Bean
  @Public
  public AuditConfiguration auditConfiguration() {
    return AuditConfiguration.builder()
      .packageToScan("pico.erp.item")
      .entity(ROLE.class)
      .build();
  }

  @Override
  public int getOrder() {
    return 4;
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Bean
  @Public
  public Role itemAccessorRole() {
    return ROLE.ITEM_ACCESSOR;
  }

  @Bean
  @Public
  public Role itemManagerRole() {
    return ROLE.ITEM_MANAGER;
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}
