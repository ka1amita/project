package com.matejkala.dtos.responsedtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshResponseTokenDTO extends ResponseDTO {
  @JsonProperty("refresh_token")
  private String refreshToken;

  public RefreshResponseTokenDTO(String token) {
    refreshToken =token;
  }

  public String getRefreshToken() {
    return refreshToken;
  }
}
