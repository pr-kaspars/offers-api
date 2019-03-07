package com.github.prkaspars.selling.resource;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Optional;

@ControllerAdvice
public class OptionalAdvice implements ResponseBodyAdvice {

  @Override
  public boolean supports(MethodParameter methodParameter, Class aClass) {
    return methodParameter.getParameterType().equals(Optional.class);
  }

  @Override
  public Object beforeBodyWrite(Object o, MethodParameter parameter, MediaType mediaType, Class aClass, ServerHttpRequest request, ServerHttpResponse response) {
    if (parameter.isOptional()) {
      return ((Optional<?>) o).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
    }
    return o;
  }
}
