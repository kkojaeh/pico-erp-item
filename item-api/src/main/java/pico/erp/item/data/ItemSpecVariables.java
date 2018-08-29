package pico.erp.item.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.io.Serializable;

@JsonTypeInfo(use = Id.CLASS, property = "@type")
public interface ItemSpecVariables extends Serializable {

  @JsonIgnore
  String getSummary();

  @JsonIgnore
  boolean isValid();

}
