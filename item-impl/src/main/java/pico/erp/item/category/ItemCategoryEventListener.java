package pico.erp.item.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.category.ItemCategoryEvents.ParentChangedEvent;

@Slf4j
@SuppressWarnings("unused")
@Component
@Transactional
public class ItemCategoryEventListener {

  private static final String LISTENER_NAME = "listener.item-category-event-listener";

  @Autowired
  private ItemCategoryServiceLogic itemCategoryService;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ParentChangedEvent.CHANNEL)
  public void onItemCategoryParentChanged(ItemCategoryEvents.ParentChangedEvent event) {
    itemCategoryService.cascadeReset(
      ItemCategoryServiceLogic.CascadeResetRequest.builder()
        .id(event.getItemCategoryId())
        .build()
    );
  }

}
