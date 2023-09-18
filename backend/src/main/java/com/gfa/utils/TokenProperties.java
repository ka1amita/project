package com.gfa.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "token")
public class TokenProperties {
  long accessExpiration;
  long refreshExpiration;
  private String secret;

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public long getAccessExpiration() {
    return accessExpiration;
  }

  public void setAccessExpiration(long accessExpiration) {
    this.accessExpiration = accessExpiration;
  }

  public long getRefreshExpiration() {
    return refreshExpiration;
  }

  public void setRefreshExpiration(long refreshExpiration) {
    this.refreshExpiration = refreshExpiration;
  }
}
