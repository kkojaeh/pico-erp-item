package pico.erp.item.category;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.audit.AuditService;
import pico.erp.item.category.ItemCategoryExceptions.NotFoundException;
import pico.erp.item.category.ItemCategoryRequests.CreateRequest;
import pico.erp.item.category.ItemCategoryRequests.DeleteRequest;
import pico.erp.item.category.ItemCategoryRequests.UpdateRequest;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Slf4j
@Service
@Public
@Transactional
@Validated
public class ItemCategoryServiceLogic implements ItemCategoryService {

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ItemCategoryMapper mapper;

  @Lazy
  @Autowired
  private AuditService auditService;

  public void cascadeReset(CascadeResetRequest request) {
    val id = request.getId();
    val itemCategory = itemCategoryRepository.findBy(id)
      .orElseThrow(NotFoundException::new);
    itemCategoryRepository.findChildrenBy(id)
      .forEach(child -> {
        val response = child.apply(new ItemCategoryMessages.SetParentRequest(itemCategory));
        itemCategoryRepository.update(child);
        auditService.commit(child);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public ItemCategoryData create(CreateRequest request) {
    val itemCategory = new ItemCategory();
    if (itemCategoryRepository.exists(request.getId())) {
      throw new ItemCategoryExceptions.AlreadyExistsException();
    }
    val response = itemCategory.apply(mapper.map(request));

    if (itemCategoryRepository.exists(itemCategory.getCode())) {
      throw new ItemCategoryExceptions.CodeAlreadyExistsException();
    }
    val created = itemCategoryRepository.create(itemCategory);
    auditService.commit(created);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val itemCategory = itemCategoryRepository.findBy(request.getId())
      .orElseThrow(ItemCategoryExceptions.NotFoundException::new);
    val response = itemCategory.apply(mapper.map(request));
    itemCategoryRepository.deleteBy(request.getId());
    auditService.delete(itemCategory);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(ItemCategoryCode code) {
    return itemCategoryRepository.exists(code);
  }

  @Override
  public boolean exists(ItemCategoryId id) {
    return itemCategoryRepository.exists(id);
  }

  @Override
  public ItemCategoryData get(ItemCategoryCode code) {
    return itemCategoryRepository.findBy(code)
      .map(mapper::map)
      .orElseThrow(ItemCategoryExceptions.NotFoundException::new);
  }

  @Override
  public ItemCategoryData get(ItemCategoryId id) {
    return itemCategoryRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(ItemCategoryExceptions.NotFoundException::new);
  }

  @Override
  public void update(UpdateRequest request) {
    val itemCategory = itemCategoryRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);

    val response = itemCategory.apply(mapper.map(request));

    itemCategoryRepository.update(itemCategory);
    auditService.commit(itemCategory);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Getter
  @Builder
  public static class CascadeResetRequest {

    @Valid
    @NotNull
    ItemCategoryId id;

  }

}
