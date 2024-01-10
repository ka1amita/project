package com.matejkala.configurations;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@ConfigurationProperties(prefix = "token")
public class TokenConfig {

  @Max(value = 10000000000L)
  @Min(1)
  long accessExpiration = 1000;
  @Max(value = 10000000000L)
  @Min(1)
  long refreshExpiration = 1000;

  @Length(min = 6,max = 417)
  private String secret = "CK861C/4haGlvyms7/ndWCgoCfM5diKBvxrdeu4FDWGSRFlh3QxiIvan0PiudLsw56UXkMgTMCyicnKTS3T8rpiHJS1AXSl6o2J8G+0ODWESWLILJuF16mNTaryiIaXb0QtyQp9aIQdI5XlFbUIJCPlYVTXS6+ZiC5WgKNdHGljSULn9Fsni2PTc6z0HEu2aALRF4+Fxb4sve6ZxLCJY+rhxzKdKQndF9QM2bgx+O+yd44jzJMkuLAG4ohcLX2y7VHNJjgc3Gztj7qGvofdUDZN8XhJIjkFRkeKSzMv12t+haHdj52JnLJUwCqFrQ14dFL7zdhJ49fCSKuaiwf+yApgJxzzPjiHGvGMoG6LUqR8=";

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
