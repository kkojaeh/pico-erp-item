package pico.erp.item.lot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "value")
@ToString
public class ItemLotId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @NotNull
  private UUID value;

  @JsonCreator
  public static ItemLotId from(@NonNull String value) {
    try {
      return new ItemLotId(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return new ItemLotId(UUID.nameUUIDFromBytes(value.getBytes()));
    }
  }

  public static ItemLotId from(@NonNull UUID value) {
    return new ItemLotId(value);
  }

  public static ItemLotId generate() {
    return from(UUID.randomUUID());
  }

}
