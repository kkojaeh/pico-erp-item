package pico.erp.item;

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
public class ItemId implements Serializable {

  private static final long serialVersionUID = 1L;

  @Getter(onMethod = @__({@JsonValue}))
  @NotNull
  private UUID value;

  @JsonCreator
  public static ItemId from(@NonNull String value) {
    try {
      return new ItemId(UUID.fromString(value));
    } catch (IllegalArgumentException e) {
      return new ItemId(UUID.nameUUIDFromBytes(value.getBytes()));
    }
  }

  public static ItemId from(@NonNull UUID value) {
    return new ItemId(value);
  }

  public static ItemId generate() {
    return from(UUID.randomUUID());
  }

}
