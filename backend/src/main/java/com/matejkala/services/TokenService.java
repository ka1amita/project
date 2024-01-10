package com.matejkala.services;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.matejkala.dtos.requestdtos.RequestTokenDTO;
import com.matejkala.dtos.responsedtos.ResponseTokensDTO;
import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public interface TokenService {
  DecodedJWT decodeToken(String token);

  String createRefreshToken(String username, Calendar now, String issuer);

  @NotNull
  Set<SimpleGrantedAuthority> getGrantedAuthorities(String token);

  Algorithm getAlgorithm();

  JWTVerifier getVerifier();

  String getPrefix();

  String getSubject(String token);

  <T extends GrantedAuthority> String createAccessToken(String username, Calendar now, String issuer,
                                                        Collection<T> authorities);

  RequestTokenDTO mapToDto(HttpServletRequest request);

  ResponseTokensDTO refreshTokens(RequestTokenDTO tokenDto);

  Authentication getAuthentication(RequestTokenDTO requestTokenDTO);

  ResponseTokensDTO mapToDto(String accessToken, String refresh_token);

  <T extends GrantedAuthority> ResponseTokensDTO createTokens(String username, String issuer,
                                 Collection<T> authorities);
}


