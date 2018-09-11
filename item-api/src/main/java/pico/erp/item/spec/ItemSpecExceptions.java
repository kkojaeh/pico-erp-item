package pico.erp.item.spec;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ItemSpecExceptions {

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item.spec.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
