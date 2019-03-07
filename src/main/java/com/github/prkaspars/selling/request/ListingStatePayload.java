package com.github.prkaspars.selling.request;

import com.github.prkaspars.selling.model.Listing;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class ListingStatePayload {
  @NotEmpty
  @Pattern(regexp = "^CANCELLED$", message = "allows only CANCELLED")
  private String state;

  public Listing.State getState() {
    return Listing.State.valueOf(state);
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setState(Listing.State state) {
    this.state = state.toString();
  }

}
