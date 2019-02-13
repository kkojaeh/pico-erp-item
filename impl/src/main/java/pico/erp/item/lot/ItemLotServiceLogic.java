package pico.erp.item.lot;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.audit.AuditService;
import pico.erp.item.lot.ItemLotExceptions.AlreadyExistsException;
import pico.erp.item.lot.ItemLotExceptions.CodeAlreadyExistsException;
import pico.erp.item.lot.ItemLotExceptions.NotFoundException;
import pico.erp.item.lot.ItemLotRequests.CreateRequest;
import pico.erp.item.lot.ItemLotRequests.DeleteRequest;
import pico.erp.item.lot.ItemLotRequests.ExpireRequest;
import pico.erp.item.lot.ItemLotRequests.UpdateRequest;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Slf4j
@Service
@Public
@Transactional
@Validated
public class ItemLotServiceLogic implements ItemLotService {

  @Autowired
  private ItemLotRepository itemLotRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private ItemLotMapper mapper;

  @Lazy
  @Autowired
  private AuditService auditService;

  @Override
  public ItemLotData create(CreateRequest request) {
    val itemLot = new ItemLot();

    if (itemLotRepository.exists(request.getId())) {
      throw new AlreadyExistsException();
    }

    val response = itemLot.apply(mapper.map(request));

    if (itemLotRepository.exists(itemLot.getKey())) {
      throw new CodeAlreadyExistsException();
    }
    val created = itemLotRepository.create(itemLot);
    auditService.commit(created);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(created);
  }

  @Override
  public void delete(DeleteRequest request) {
    val itemLot = itemLotRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    itemLotRepository.deleteBy(request.getId());
    auditService.delete(itemLot);
    eventPublisher.publishEvents(response.getEvents());
  }

  @Override
  public boolean exists(ItemLotKey key) {
    return itemLotRepository.exists(key);
  }

  @Override
  public boolean exists(ItemLotId id) {
    return itemLotRepository.exists(id);
  }

  @Override
  public void expire(ExpireRequest request) {
    itemLotRepository.findAllExpireCandidatesBeforeThan(request.getFixedDate())
      .forEach(itemLot -> {
        val response = itemLot.apply(mapper.map(request));
        itemLotRepository.update(itemLot);
        auditService.commit(itemLot);
        eventPublisher.publishEvents(response.getEvents());
      });
  }

  @Override
  public ItemLotData get(ItemLotKey key) {
    return itemLotRepository.findBy(key)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public ItemLotData get(ItemLotId id) {
    return itemLotRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(NotFoundException::new);
  }

  @Override
  public List<ItemLotData> getAll(Iterable<ItemLotId> ids) {
    return itemLotRepository.findAllBy(ids)
      .map(mapper::map)
      .collect(Collectors.toList());
  }

  @Override
  public void update(UpdateRequest request) {
    val itemLot = itemLotRepository.findBy(request.getId())
      .orElseThrow(NotFoundException::new);
    val response = itemLot.apply(mapper.map(request));
    itemLotRepository.update(itemLot);
    auditService.commit(itemLot);
    eventPublisher.publishEvents(response.getEvents());
  }
}
