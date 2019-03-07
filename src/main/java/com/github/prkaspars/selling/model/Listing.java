package com.github.prkaspars.selling.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Listing {
  public enum State {
    ACTIVE, CANCELLED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @OneToOne
  @JoinColumn
  private Offer offer;
  private LocalDate expires;
  private State state;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
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
