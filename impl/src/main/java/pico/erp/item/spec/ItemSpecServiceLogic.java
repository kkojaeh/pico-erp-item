package pico.erp.item.spec;

import java.math.BigDecimal;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.spec.ItemSpecRequests.CalculatePurchaseQuantityRequest;
import pico.erp.item.spec.ItemSpecRequests.CreateRequest;
import pico.erp.item.spec.ItemSpecRequests.DeleteRequest;
import pico.erp.item.spec.ItemSpecRequests.LockRequest;
import pico.erp.item.spec.ItemSpecRequests.UnlockRequest;
import pico.erp.item.spec.ItemSpecRequests.UpdateRequest;
import pico.erp.shared.event.EventPublisher;

@Service
@ComponentBean
@Transactional
@Validated
public class ItemSpecServiceLogic implements ItemSpecService {

  @Autowired
  private ItemSpecRepository itemSpecRepository;

  @Autowired
  private ItemSpecMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

  @Override
  public BigDecimal calculate(CalculatePurchaseQuantityRequest request) {
    val itemSpec = itemSpecRepository.findBy(request.getId())
      .orElseThrow(ItemSpecExceptions.NotFoundException::new);
    return itemSpec.calculatePurchaseQuantity(request.getQuantity());
  }

  @Override
  public ItemSpecData create(CreateRequest request) {
    val itemSpec = new ItemSpec();
    val response = itemSpec.apply(mapper.map(request));
    val created = itemSpecRepository.create(itemSpec);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val itemSpec = itemSpecRepository.findBy(request.getId())
      .orElseThrow(ItemSpecExceptions.NotFoundException::new);
    val response = itemSpec.apply(mapper.map(request));
    itemSpecRepository.deleteBy(request.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(ItemSpecId id) {
    return itemSpecRepository.exists(id);
  }

  @Override
  public ItemSpecData get(ItemSpecId id) {
    return itemSpecRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ItemSpecExceptions.NotFoundException::new);
  }

  @Override
  public void lock(LockRequest request) {
    val itemSpec = itemSpecRepository.findBy(request.getId())
      .orElseThrow(ItemSpecExceptions.NotFoundException::new);
    val response = itemSpec.apply(mapper.map(request));
    itemSpecRepository.update(itemSpec);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void unlock(UnlockRequest request) {
    val itemSpec = itemSpecRepository.findBy(request.getId())
      .orElseThrow(ItemSpecExceptions.NotFoundException::new);
    val response = itemSpec.apply(mapper.map(request));
    itemSpecRepository.update(itemSpec);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void update(UpdateRequest request) {
    val itemSpec = itemSpecRepository.findBy(request.getId())
      .orElseThrow(ItemSpecExceptions.NotFoundException::new);
    val response = itemSpec.apply(mapper.map(request));
    itemSpecRepository.update(itemSpec);
    eventPublisher.publishEvents(response.getEvents());
  }
}
