/**
 * 
 */
package com.megamus.app.ws.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.megamus.app.ws.model.response.ErrorMessage;

/**
 * @author mrens
 *
 */
@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = { UserServiceException.class })
    public ResponseEntity<Object> handlerUserServiceException(UserServiceException ex, WebRequest webRequest) {

        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
