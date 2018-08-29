package pico.erp.item.core;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.ItemSpecExceptions.NotFoundException;
import pico.erp.item.ItemSpecRequests.CreateRequest;
import pico.erp.item.ItemSpecRequests.DeleteRequest;
import pico.erp.item.ItemSpecRequests.UpdateRequest;
import pico.erp.item.ItemSpecService;
import pico.erp.item.data.ItemSpecData;
import pico.erp.item.data.ItemSpecId;
import pico.erp.item.domain.ItemSpec;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@Service
@Public
@Transactional
@Validated
public class ItemSpecServiceLogic implements ItemSpecService {

  @Autowired
  private ItemSpecRepository itemSpecRepository;

  @Autowired
  private ItemMapper mapper;

  @Autowired
  private EventPublisher eventPublisher;

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
      .orElseThrow(NotFoundException::new);
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
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public void update(UpdateRequest request) {
    val itemSpec = itemSpecRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = itemSpec.apply(mapper.map(request));
    itemSpecRepository.update(itemSpec);
    eventPublisher.publishEvents(response.getEvents());
  }
}
