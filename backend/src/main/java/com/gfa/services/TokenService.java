package com.gfa.services;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gfa.dtos.requestdtos.RequestTokenDTO;
import com.gfa.dtos.responsedtos.ResponseTokenDTO;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface TokenService {
  DecodedJWT decodeJwt(String jwt);

  String createToken(String username, Calendar now, String issuer);

  Algorithm getAlgorithm();

  JWTVerifier getVerifier();

  @NotNull
  Authentication getAuthenticationToken(String token);

  String getPrefix();

  Set<SimpleGrantedAuthority> getGrantedAuthorities(DecodedJWT decodedJwt);

  String getSubject(String jwt);

  <T extends GrantedAuthority> String createToken(String username, Calendar now, String issuer,
                                                  Collection<T> authorities);

  RequestTokenDTO parse(HttpServletRequest request);

  Set<ResponseTokenDTO> refreshTokens(RequestTokenDTO tokenDto);
}


