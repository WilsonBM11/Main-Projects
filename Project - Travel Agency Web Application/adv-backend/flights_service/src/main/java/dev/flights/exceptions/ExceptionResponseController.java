package dev.flights.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResponseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseController.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                ex.getCode()
        );
        LOGGER.error(response.message(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Throwable ex) {
        ErrorResponse response = new ErrorResponse(
                ex.getMessage(),
                ErrorCodes.UNKNOWN_ERROR
        );
        LOGGER.error(response.message(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
