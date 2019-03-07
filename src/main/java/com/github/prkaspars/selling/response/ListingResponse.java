package com.github.prkaspars.selling.response;

import com.github.prkaspars.selling.model.Listing;

import java.time.LocalDate;
import java.util.Currency;

public class ListingResponse {
  private Integer id;
  private String name;
  private String description;
  private Currency currency;
  private Double price;
  private LocalDate expires;

  public ListingResponse() {
  }

  public ListingResponse(Listing listing) {
    id = listing.getId();
    name = listing.getOffer().getName();
    description = listing.getOffer().getDescription();
    currency = listing.getOffer().getCurrency();
    price = listing.getOffer().getPrice();
    expires = listing.getExpires();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Currency getCurrency() {
    return currency;
  }

  public void setCurrency(Currency currency) {
    this.currency = currency;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public LocalDate getExpires() {
    return expires;
  }

  public void setExpires(LocalDate expires) {
    this.expires = expires;
  }
}
