package com.github.prkaspars.selling.resource;

import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.request.CreateOfferPayload;
import com.github.prkaspars.selling.request.PatchStatePayload;
import com.github.prkaspars.selling.response.OfferResponse;
import com.github.prkaspars.selling.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class OfferResource {
  private OfferService service;

  @Autowired
  public OfferResource(OfferService service) {
    this.service = service;
  }

  @GetMapping("/offers")
  public List<OfferResponse> active() {
    return service.active().stream().map(OfferResponse::new).collect(Collectors.toList());
  }

  @PostMapping("/offers")
  @ResponseStatus(HttpStatus.CREATED)
  public OfferResponse create(@Valid @RequestBody CreateOfferPayload payload, HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder) {
    Offer offer = service.list(payload);
    String location = uriComponentsBuilder.path("/offers/{id}").buildAndExpand(offer.getId()).toUri().toString();
    response.addHeader(HttpHeaders.LOCATION, location);
    return new OfferResponse(offer);
  }

  @GetMapping("/offers/{id}")
  public Optional<OfferResponse> read(@PathVariable Integer id) {
    return service.read(id).map(OfferResponse::new);
  }

  @PatchMapping("/offers/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancel(@Valid @RequestBody PatchStatePayload payload, @PathVariable Integer id) {
    if (!service.cancel(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }
  }
}
