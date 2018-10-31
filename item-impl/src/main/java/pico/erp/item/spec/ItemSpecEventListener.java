package pico.erp.item.spec;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.item.ItemEvents;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("unused")
@Component
@Transactional
public class ItemSpecEventListener {

  private static final String LISTENER_NAME = "listener.item-spec-event-listener";

  @Autowired
  private ItemSpecRepository itemSpecRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ItemEvents.UpdatedEvent.CHANNEL)
  public void onItemCategoryParentChanged(ItemEvents.UpdatedEvent event) {
    val itemId = event.getItemId();
    itemSpecRepository.findAllBy(itemId)
      .forEach(itemSpec -> {
        if (!itemSpec.isLocked()) {
          val response = itemSpec.apply(new ItemSpecMessages.RecalculateRequest());
          itemSpecRepository.update(itemSpec);
          eventPublisher.publishEvents(response.getEvents());
        }
      });
  }


}
