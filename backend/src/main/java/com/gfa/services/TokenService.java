package com.gfa.services;

import com.auth0.jwt.algorithms.Algorithm;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {
  long getAccessExp();

  long getRefreshExp();

  String getSecret();

  String getToken(String username, String issuer, long accessExpiration,
                  Collection<GrantedAuthority> authorities, Algorithm algorithm);

  String getToken(String issuer, String username, long refreshExpiration,
                  Algorithm algorithm);

  Algorithm getAlgorithm();
}
