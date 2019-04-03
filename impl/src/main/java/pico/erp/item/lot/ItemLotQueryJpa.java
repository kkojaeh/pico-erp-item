package pico.erp.item.lot;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.ComponentBean;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.lot.ItemLotView.Filter;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@ComponentBean
@Transactional(readOnly = true)
@Validated
public class ItemLotQueryJpa implements ItemLotQuery {

  private final QItemLotEntity itemLot = QItemLotEntity.itemLotEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public Page<ItemLotView> retrieve(Filter filter, Pageable pageable) {

    val query = new JPAQuery<ItemLotView>(entityManager);
    val select = Projections.bean(ItemLotView.class,
      itemLot.id,
      itemLot.itemId,
      itemLot.specCode,
      itemLot.lotCode,
      itemLot.expirationDate,
      itemLot.expired,
      itemLot.expiredDate,
      itemLot.createdBy,
      itemLot.createdDate
    );
    query.select(select);
    query.from(itemLot);

    val builder = new BooleanBuilder();

    builder.and(itemLot.itemId.eq(filter.getItemId()));

    if (!isEmpty(filter.getCode())) {
      builder.and(
        itemLot.lotCode.value
          .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%"))
          .or(
            itemLot.specCode.value
              .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%"))
          )
      );
    }

    if (filter.getExpired() != null) {
      builder.and(itemLot.expired.eq(filter.getExpired()));
    }
    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }
}
