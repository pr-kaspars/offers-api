package com.github.prkaspars.selling.response;

import com.github.prkaspars.selling.model.Offer;

import java.time.LocalDate;
import java.util.Currency;

public class OfferResponse {
  private Integer id;
  private String name;
  private String description;
  private Currency currency;
  private Double price;
  private LocalDate expires;
  private Offer.State state;

  public OfferResponse() {
  }

  public OfferResponse(Offer offer) {
    id = offer.getId();
    name = offer.getProduct().getName();
    description = offer.getProduct().getDescription();
    currency = offer.getProduct().getCurrency();
    price = offer.getProduct().getPrice();
    expires = offer.getExpires();
    state = offer.getState();
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

  public Offer.State getState() {
    return state;
  }

  public void setState(Offer.State state) {
    this.state = state;
  }
}
