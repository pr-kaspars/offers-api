package com.github.prkaspars.selling.response;

import java.util.List;

public class ErrorResponse {
  private List<Failure> failures;

  public ErrorResponse(List<Failure> failures) {
    this.failures = failures;
  }

  public List<Failure> getFailures() {
    return failures;
  }

  public void setFailures(List<Failure> failures) {
    this.failures = failures;
  }
}
