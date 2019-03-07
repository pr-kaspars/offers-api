package com.github.prkaspars.selling.resource;

import com.github.prkaspars.selling.model.Listing;
import com.github.prkaspars.selling.request.ListingStatePayload;
import com.github.prkaspars.selling.request.OfferPayload;
import com.github.prkaspars.selling.response.ListingResponse;
import com.github.prkaspars.selling.service.TradeService;
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
public class TradeResource {
  private TradeService tradeService;

  @Autowired
  public TradeResource(TradeService tradeService) {
    this.tradeService = tradeService;
  }

  @GetMapping("/listings")
  public List<ListingResponse> active() {
    return tradeService.active().stream().map(ListingResponse::new).collect(Collectors.toList());
  }

  @PostMapping("/listings")
  @ResponseStatus(HttpStatus.CREATED)
  public ListingResponse create(@Valid @RequestBody OfferPayload payload, HttpServletResponse response, UriComponentsBuilder uriComponentsBuilder) {
    Listing listing = tradeService.list(payload);
    String location = uriComponentsBuilder.path("/listings/{id}").buildAndExpand(listing.getId()).toUri().toString();
    response.addHeader(HttpHeaders.LOCATION, location);
    return new ListingResponse(listing);
  }

  @GetMapping("/listings/{id}")
  public Optional<ListingResponse> read(@PathVariable Integer id) {
    return tradeService.read(id).map(ListingResponse::new);
  }

  @PatchMapping("/listings/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void cancel(@Valid @RequestBody ListingStatePayload payload, @PathVariable Integer id) {
    if (!tradeService.cancel(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
    }
  }

}
