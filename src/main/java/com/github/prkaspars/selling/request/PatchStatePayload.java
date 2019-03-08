package com.github.prkaspars.selling.request;

import com.github.prkaspars.selling.model.Offer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class PatchStatePayload {
  @NotEmpty
  @Pattern(regexp = "^CANCELLED$", message = "allows only CANCELLED")
  private String state;

  public Offer.State getState() {
    return Offer.State.valueOf(state);
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setState(Offer.State state) {
    this.state = state.toString();
  }

}
