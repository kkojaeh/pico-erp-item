package pico.erp.item;

import static org.springframework.util.StringUtils.isEmpty;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.item.category.ItemCategoryExceptions.NotFoundException;
import pico.erp.item.category.ItemCategoryRepository;
import pico.erp.item.category.QItemCategoryEntity;
import pico.erp.shared.LabeledValue;
import pico.erp.shared.Public;
import pico.erp.shared.QExtendedLabeledValue;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Public
@Transactional(readOnly = true)
@Validated
public class ItemQueryJpa implements ItemQuery {

  private final QItemEntity item = QItemEntity.itemEntity;

  private final QItemCategoryEntity itemCategory = QItemCategoryEntity.itemCategoryEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Autowired
  private ItemCategoryRepository itemCategoryRepository;

  @Override
  public List<? extends LabeledValuable> asLabels(String keyword, long limit) {
    val query = new JPAQuery<LabeledValue>(entityManager);

    val select = new QExtendedLabeledValue(
      item.id.value.as("value"),
      item.name.as("label"),
      itemCategory.name.nullif("N/A").as("subLabel"),
      item.code.value.as("stamp")
    );
    query.select(select);
    query.from(item);
    query.leftJoin(itemCategory)
      .on(item.categoryId.eq(itemCategory.id));
    query
      .where(item.name.likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", keyword, "%"))
        .and(item.status.eq(ItemStatusKind.ACTIVATED)));
    query.limit(limit);
    query.orderBy(item.name.asc());
    return query.fetch();
  }

  @Override
  public Page<ItemView> retrieve(ItemView.Filter filter, Pageable pageable) {
    val query = new JPAQuery<ItemView>(entityManager);
    val select = Projections.bean(ItemView.class,
      item.id,
      item.code,
      item.name,
      item.unit,
      item.type,
      item.externalCode,
      item.barcodeNumber,
      itemCategory.id.as("categoryId"),
      item.customerId,
      item.status,
      item.createdBy,
      item.createdDate
    );
    query.select(select);
    query.from(item);
    query.leftJoin(itemCategory)
      .on(item.categoryId.eq(itemCategory.id));

    val builder = new BooleanBuilder();

    if (!isEmpty(filter.getName())) {
      builder.and(
        item.name
          .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getName(), "%")));
    }
    if (!isEmpty(filter.getCode())) {
      builder.and(
        item.code.value
          .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%"))
          .or(item.externalCode
            .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%")))
          .or(item.barcodeNumber
            .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", filter.getCode(), "%"))));
    }
    if (filter.getStatuses() != null && !filter.getStatuses().isEmpty()) {
      builder.and(item.status.in(filter.getStatuses()));
    }
    if (filter.getTypes() != null && !filter.getTypes().isEmpty()) {
      builder.and(item.type.in(filter.getTypes()));
    }
    if (!isEmpty(filter.getCategoryId())) {
      val category = new QItemCategoryEntity("category");
      val itemCategory = itemCategoryRepository.findBy(filter.getCategoryId())
        .orElseThrow(NotFoundException::new);
      builder.and(item.categoryId.in(
        JPAExpressions.select(category.id)
          .from(category)
          .where(
            category.key.like(queryDslJpaSupport.toLikeKeyword("", itemCategory.getKey(), "%")))
      ));
    }
    if (!isEmpty(filter.getCustomerId())) {
      builder.and(item.customerId.eq(filter.getCustomerId()));
    }

    if (filter.getPurchasable() != null) {
      builder.and(item.purchasable.eq(filter.getPurchasable()));
    }

    if (filter.getSalable() != null) {
      builder.and(item.salable.eq(filter.getSalable()));
    }

    query.where(builder);
    return queryDslJpaSupport.paging(query, pageable, select);
  }

}
