package pico.erp.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public interface ItemExceptions {

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.already.exists.exception")
  class ItemAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.cannot.activate.exception")
  class ItemCannotActivateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.cannot.deactivate.exception")
  class ItemCannotDeactivateException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.cannot.specify.exception")
  class ItemCannotSpecifyException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "item.code.already.exists.exception")
  class ItemCodeAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "item.not.found.exception")
  class ItemNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

  }
}
