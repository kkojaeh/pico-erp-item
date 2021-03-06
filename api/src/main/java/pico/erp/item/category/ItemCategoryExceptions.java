package pico.erp.item.category;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ItemCategoryExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.category.already.exists.exception")
  class AlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.category.code.already.exists.exception")
  class CodeAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item.category.not.found.exception")
  class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
