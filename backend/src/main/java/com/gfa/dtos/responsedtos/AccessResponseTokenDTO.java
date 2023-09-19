package com.gfa.dtos.responsedtos;

public class AccessResponseTokenDTO extends ResponseTokenDTO {

  private String access_token;

  public AccessResponseTokenDTO(String token) {
    access_token = token;
  }
  public String getToken() {
    return access_token;
  }
}
