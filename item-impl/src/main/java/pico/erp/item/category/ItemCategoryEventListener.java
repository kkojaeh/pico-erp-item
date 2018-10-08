package pico.erp.item.category;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pico.erp.audit.AuditService;
import pico.erp.item.ItemMapper;
import pico.erp.item.category.ItemCategoryEvents.ParentChangedEvent;
import pico.erp.item.category.ItemCategoryExceptions.NotFoundException;
import pico.erp.shared.event.EventPublisher;

@Slf4j
@SuppressWarnings("unused")
@Component
@Transactional
public class ItemCategoryEventListener {

  private static final String LISTENER_NAME = "listener.item-category-event-listener";

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Lazy
  @Autowired
  private AuditService auditService;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ItemMapper itemMapper;

  @EventListener
  @JmsListener(destination = LISTENER_NAME + "."
    + ParentChangedEvent.CHANNEL)
  public void onItemCategoryParentChanged(ParentChangedEvent event) {
    ItemCategoryId id = event.getItemCategoryId();
    ItemCategory itemCategory = itemCategoryRepository.findBy(id)
      .orElseThrow(NotFoundException::new);
    itemCategoryRepository.findChildrenBy(id)
      .forEach(child -> {
        val response = child.apply(new ItemCategoryMessages.SetParentRequest(itemCategory));
        itemCategoryRepository.update(child);
        auditService.commit(child);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

}
