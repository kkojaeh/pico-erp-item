package pico.erp.item.category;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kkojaeh.spring.boot.component.Give;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.shared.LabeledValue;
import pico.erp.shared.QExtendedLabeledValue;
import pico.erp.shared.data.LabeledValuable;
import pico.erp.shared.jpa.QueryDslJpaSupport;

@Service
@Give
@Transactional(readOnly = true)
@Validated
public class ItemCategoryQueryJpa implements ItemCategoryQuery {

  private final QItemCategoryEntity itemCategory = QItemCategoryEntity.itemCategoryEntity;

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private QueryDslJpaSupport queryDslJpaSupport;

  @Override
  public List<? extends LabeledValuable> asLabels(String keyword, long limit) {
    val query = new JPAQuery<LabeledValue>(entityManager);
    val select = new QExtendedLabeledValue(
      itemCategory.id.value.as("value"),
      itemCategory.name.as("label"),
      itemCategory.path.as("subLabel"),
      itemCategory.code.value.as("stamp")
    );
    query.select(select);
    query.from(itemCategory);
    query.where(
      itemCategory.name.likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", keyword, "%"))
        .or(itemCategory.code.value
          .likeIgnoreCase(queryDslJpaSupport.toLikeKeyword("%", keyword, "%")))
    );
    query.limit(limit);
    query.orderBy(itemCategory.name.asc());
    return query.fetch();
  }

  @Override
  public List<ItemCategoryHierarchyView> findAllAsHierarchy() {
    val query = new JPAQuery<ItemCategoryHierarchyView>(entityManager);
    val select = Projections.bean(ItemCategoryHierarchyView.class,
      itemCategory.id,
      itemCategory.code,
      itemCategory.name,
      itemCategory.parentId,
      itemCategory.path,
      itemCategory.itemCount,
      itemCategory.createdBy,
      itemCategory.createdDate,
      itemCategory.lastModifiedBy,
      itemCategory.lastModifiedDate
    );

    val views = new HashMap<ItemCategoryId, ItemCategoryHierarchyView>();

    val childrenMap = new HashMap<ItemCategoryId, List<ItemCategoryHierarchyView>>();

    query.select(select);
    query.from(itemCategory);
    query.iterate().forEachRemaining(v -> {
      if (!childrenMap.containsKey(v.getParentId())) {
        childrenMap.put(v.getParentId(), new LinkedList<>());
      }
      childrenMap.get(v.getParentId()).add(v);
      views.put(v.getId(), v);
    });
    childrenMap.forEach((parentId, children) -> {
      Collections.sort(children);
      if (parentId != null) {
        ItemCategoryHierarchyView parent = views.get(parentId);
        parent.setChildren(children);
      }
    });
    return childrenMap.containsKey(null) ? childrenMap.get(null) : Collections.emptyList();
  }

}
