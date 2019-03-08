package com.github.prkaspars.selling.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Offer {
  public enum State {
    ACTIVE, CANCELLED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  @OneToOne
  @JoinColumn
  private Product product;
  private LocalDate expires;
  private State state;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
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
