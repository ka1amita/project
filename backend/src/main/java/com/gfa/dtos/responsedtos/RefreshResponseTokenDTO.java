package com.gfa.dtos.responsedtos;

public class RefreshResponseTokenDTO extends ResponseDTO {
  private String refresh_token;

  public RefreshResponseTokenDTO(String token) {
    refresh_token=token;
  }

  public String getToken() {
    return refresh_token;
  }
}
