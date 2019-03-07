package com.github.prkaspars.selling.resource;

import com.github.prkaspars.selling.model.Offer;
import com.github.prkaspars.selling.request.OfferPayload;
import com.github.prkaspars.selling.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class TradeResource {
  private TradeService tradeService;

  @Autowired
  public TradeResource(TradeService tradeService) {
    this.tradeService = tradeService;
  }

  @PostMapping("/listings")
  @ResponseStatus(HttpStatus.CREATED)
  public Offer create(@Valid @RequestBody OfferPayload payload, HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder) {
    Offer offer = tradeService.listOffer(payload);
    String location = uriComponentsBuilder.path("/listings/{id}").buildAndExpand(offer.getId()).toUri().toString();
    response.addHeader(HttpHeaders.LOCATION, location);
    return offer;
  }
}
