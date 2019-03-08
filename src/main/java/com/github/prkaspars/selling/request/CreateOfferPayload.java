package com.github.prkaspars.selling.request;

import javax.validation.constraints.*;

public class CreateOfferPayload {
  @NotEmpty
  private String name;
  @NotEmpty
  private String description;
  @NotEmpty
  @Pattern(regexp = "^(EUR|JPY|GBP|USD)$", message = "unsupported currency")
  private String currency;
  @NotNull
  @Min(value = 0, message = "must be non negative number")
  @Digits(integer = 16, fraction = 2, message = "invalid format")
  private Double price;
  @NotNull
  @Min(value = 1, message = "must be positive integer")
  private Integer duration;

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

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(Integer duration) {
    this.duration = duration;
  }
}
