package com.github.prkaspars.selling.resource;

import com.github.prkaspars.selling.response.ErrorResponse;
import com.github.prkaspars.selling.response.Failure;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse processValidationError(MethodArgumentNotValidException exception) {
    List<Failure> failures = exception.getBindingResult().getFieldErrors().stream()
      .map(Failure::new)
      .collect(Collectors.toList());
    return new ErrorResponse(failures);
  }
}
