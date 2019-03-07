package com.github.prkaspars.selling.resource;

import com.github.prkaspars.selling.model.Listing;
import com.github.prkaspars.selling.request.OfferPayload;
import com.github.prkaspars.selling.response.ListingResponse;
import com.github.prkaspars.selling.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
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
  public ListingResponse create(@Valid @RequestBody OfferPayload payload, HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder) {
    Listing listing = tradeService.list(payload);
    String location = uriComponentsBuilder.path("/listings/{id}").buildAndExpand(listing.getId()).toUri().toString();
    response.addHeader(HttpHeaders.LOCATION, location);
    return new ListingResponse(listing);
  }
}
