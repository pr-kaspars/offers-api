package com.github.prkaspars.selling.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Listing {
  public enum State {
    ACTIVE, CANCELLED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  private Integer offerId;
  private LocalDate expires;
  private State state;

  public Listing() {
  }

  public Listing(Integer id, Integer offerId, LocalDate expires, State state) {
    this.id = id;
    this.offerId = offerId;
    this.expires = expires;
    this.state = state;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getOfferId() {
    return offerId;
  }

  public void setOfferId(Integer offerId) {
    this.offerId = offerId;
  }

  public LocalDate getExpires() {
    return expires;
  }

  public void setExpires(LocalDate expires) {
    this.expires = expires;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }
}
