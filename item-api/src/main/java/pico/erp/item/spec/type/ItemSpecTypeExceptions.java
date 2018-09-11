package pico.erp.item.spec.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ItemSpecTypeExceptions {

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item.spec.type.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
