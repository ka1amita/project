package com.gfa.services;

import com.auth0.jwt.algorithms.Algorithm;
import java.util.Calendar;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public interface TokenService {
  String getToken(String username, Calendar now, String issuer);

  String getToken(String username, Calendar now, String issuer,
                  Collection<GrantedAuthority> authorities);

  @NotNull
  Authentication getAuthenticationToken(String token);
}
