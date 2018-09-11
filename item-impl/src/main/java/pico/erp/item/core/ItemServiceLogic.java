package pico.erp.item.core;

import java.io.Serializable;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.audit.AuditService;
import pico.erp.item.ItemExceptions.ItemAlreadyExistsException;
import pico.erp.item.ItemExceptions.ItemCodeAlreadyExistsException;
import pico.erp.item.ItemExceptions.ItemNotFoundException;
import pico.erp.item.ItemRequests.ActivateRequest;
import pico.erp.item.ItemRequests.CreateRequest;
import pico.erp.item.ItemRequests.DeactivateRequest;
import pico.erp.item.ItemRequests.DeleteRequest;
import pico.erp.item.ItemRequests.UpdateRequest;
import pico.erp.item.ItemService;
import pico.erp.item.category.ItemCategoryRepository;
import pico.erp.item.data.ItemCode;
import pico.erp.item.data.ItemData;
import pico.erp.item.data.ItemId;
import pico.erp.item.domain.Item;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
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

  @Lazy
  @Autowired
  private AuditService auditService;

  @Override
  public void activate(ActivateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(ItemNotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    auditService.commit(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public ItemData create(CreateRequest request) {
    val item = new Item();

    if (itemRepository.exists(request.getId())) {
      throw new ItemAlreadyExistsException();
    }

    val response = item.apply(mapper.map(request));

    if (itemRepository.exists(item.getCode())) {
      throw new ItemCodeAlreadyExistsException();
    }
    val created = itemRepository.create(item);
    auditService.commit(created);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void deactivate(DeactivateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(ItemNotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.update(item);
    auditService.commit(item);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public void delete(DeleteRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(ItemNotFoundException::new);
    val response = item.apply(mapper.map(request));
    itemRepository.deleteBy(request.getId());
    auditService.delete(item);
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
      .orElseThrow(ItemNotFoundException::new);
  }

  @Override
  public ItemData get(ItemCode code) {
    return itemRepository.findBy(code)
      .map(mapper::map)
      .orElseThrow(ItemNotFoundException::new);
  }

  @Override
  public Serializable getSpecMetadata(ItemId id) {
    Item item = itemRepository.findBy(id)
      .orElseThrow(ItemNotFoundException::new);
    return item.getSpecMetadata();
  }

  @Override
  public void update(UpdateRequest request) {
    val item = itemRepository.findBy(request.getId())
      .orElseThrow(ItemNotFoundException::new);

    val oldCode = item.getCode();

    val response = item.apply(mapper.map(request));

    // 코드가 변경 되었을 때 존재 확인
    if (!Optional.ofNullable(oldCode)
      .equals(Optional.ofNullable(item.getCode()))) {
      if (itemRepository.exists(item.getCode())) {
        throw new ItemCodeAlreadyExistsException();
      }
    }
    itemRepository.update(item);
    auditService.commit(item);

    if (response.isCategoryChanged()) {
      Stream.of(response.getOldItem().getCategory(), item.getCategory())
        .forEach(category -> {
          if (category != null) {
            itemCategoryRepository.update(category);
            auditService.commit(category);
          }
        });
    }

    eventPublisher.publishEvents(response.getEvents());
  }

}
