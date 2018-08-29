package pico.erp.item.impl.jpa;


import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pico.erp.attachment.data.AttachmentId;
import pico.erp.company.data.CompanyId;
import pico.erp.item.data.ItemCode;
import pico.erp.item.data.ItemId;
import pico.erp.item.data.ItemSpecTypeId;
import pico.erp.item.data.ItemStatusKind;
import pico.erp.item.data.ItemTypeKind;
import pico.erp.shared.TypeDefinitions;
import pico.erp.shared.data.Auditor;
import pico.erp.shared.data.UnitKind;

@Entity(name = "Item")
@Table(name = "ITM_ITEM", indexes = {
  @Index(name = "ITM_ITEM_CODE_IDX", columnList = "CODE", unique = true)
})
@Data
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ID", length = TypeDefinitions.ID_LENGTH))
  })
  ItemId id;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String name;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CODE", length = TypeDefinitions.CODE_LENGTH))
  })
  ItemCode code;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "SPEC_TYPE_ID", length = TypeDefinitions.CLASS_NAME_LENGTH))
  })
  ItemSpecTypeId specTypeId;

  @Column(length = TypeDefinitions.EXTERNAL_ID_LENGTH)
  String externalCode;

  @ManyToOne
  @JoinColumn(name = "CATEGORY_ID")
  ItemCategoryEntity category;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  UnitKind unit;

  @Column(scale = 2)
  BigDecimal baseUnitCost;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  ItemTypeKind type;

  @Column(length = TypeDefinitions.DESCRIPTION_LENGTH)
  String description;

  @Column(length = TypeDefinitions.ENUM_LENGTH)
  @Enumerated(EnumType.STRING)
  ItemStatusKind status;

  @Column
  boolean purchasable;

  @Column
  boolean sellable;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "CUSTOMER_ID", length = TypeDefinitions.ID_LENGTH))
  })
  CompanyId customerId;

  @AttributeOverrides({
    @AttributeOverride(name = "value", column = @Column(name = "ATTACHMENT_ID", length = TypeDefinitions.ID_LENGTH))
  })
  AttachmentId attachmentId;

  @Column(length = TypeDefinitions.NAME_LENGTH)
  String customerName;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "CREATED_BY_ID", updatable = false, length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "CREATED_BY_NAME", updatable = false, length = TypeDefinitions.NAME_LENGTH))
  })
  @CreatedBy
  Auditor createdBy;

  @CreatedDate
  @Column(updatable = false)
  OffsetDateTime createdDate;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "LAST_MODIFIED_BY_ID", length = TypeDefinitions.ID_LENGTH)),
    @AttributeOverride(name = "name", column = @Column(name = "LAST_MODIFIED_BY_NAME", length = TypeDefinitions.NAME_LENGTH))
  })
  @LastModifiedBy
  Auditor lastModifiedBy;

  @LastModifiedDate
  OffsetDateTime lastModifiedDate;

}
