package pico.erp.item;

import java.util.Optional;
import java.util.stream.Stream;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.ItemExceptions.AlreadyExistsException;
import pico.erp.item.ItemExceptions.CodeAlreadyExistsException;
import pico.erp.item.ItemExceptions.NotFoundException;
import pico.erp.item.ItemRequests.ActivateRequest;
import pico.erp.item.ItemRequests.CreateRequest;
import pico.erp.item.ItemRequests.DeactivateRequest;
import pico.erp.item.ItemRequests.DeleteRequest;
import pico.erp.item.ItemRequests.UpdateRequest;
import pico.erp.item.category.ItemCategoryRepository;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@ComponentBean
@Transactional
@Validated
public class ItemServiceLogic implements ItemService {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ItemMapper mapper;

  @Override
  public void activate(ActivateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public ItemData create(CreateRequest request) {
    val item = new Item();

    if (itemRepository.exists(request.getId())) {
      throw new AlreadyExistsException();
    }

    val response = item.apply(mapper.map(request));

    if (itemRepository.exists(item.getCode())) {
      throw new CodeAlreadyExistsException();
    }
    val created = itemRepository.create(item);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void deactivate(DeactivateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void delete(DeleteRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.deleteBy(request.getId());
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(ItemId id) {
    return itemRepository.exists(id);
  }

  @Override
  public boolean exists(ItemCode code) {
    return itemRepository.exists(code);
  }

  @Override
  public ItemData get(ItemId id) {
    return itemRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public ItemData get(ItemCode code) {
    return itemRepository.findBy(code)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public void update(UpdateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);

    val oldCode = item.getCode();

    val response = item.apply(mapper.map(request));

    // 코드가 변경 되었을 때 존재 확인
    if (!Optional.ofNullable(oldCode)
      .equals(Optional.ofNullable(item.getCode()))) {
      if (itemRepository.exists(item.getCode())) {
        throw new CodeAlreadyExistsException();
      }
    }
    itemRepository.update(item);

    if (response.isCategoryChanged()) {
      Stream.of(response.getOldItem().getCategory(), item.getCategory())
        .forEach(category -> {
          if (category != null) {
            itemCategoryRepository.update(category);
          }
        });
    }

    eventPublisher.publishEvents(response.getEvents());
  }

}
