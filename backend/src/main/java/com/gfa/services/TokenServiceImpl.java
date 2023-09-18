package com.gfa.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${token.expiration.access}")
  private long ACCESS_EXP;
  @Value("${token.expiration.refresh}")
  private long REFRESH_EXP;
  @Value("${token.secret}")
  private String SECRET;

  public TokenServiceImpl() {
  }

  @Override
  public String getSecret() {
    return SECRET;
  }

  @Override
  public long getAccessExp() {
    return ACCESS_EXP;
  }

  public void setAccessExp(long accessExp) {
    ACCESS_EXP = accessExp;
  }

  @Override
  public long getRefreshExp() {
    return REFRESH_EXP;
  }

  public void setRefreshExp(long refreshExp) {
    REFRESH_EXP = refreshExp;
  }

  public void setSECRET(String secret) {
    SECRET = secret;
  }

  @Override
  public String getToken(String issuer, String username,
                         long refreshExpiration,
                         Algorithm algorithm) {
    return JWT.create()
              .withSubject(username)
              .withExpiresAt(new Date(System.currentTimeMillis() +
                                      refreshExpiration))
              .withIssuer(issuer)
              .sign(algorithm);
  }

  @Override
  public Algorithm getAlgorithm() {
    return Algorithm.HMAC256(SECRET.getBytes());
  }

  @Override
  public String getToken(String username, String issuer, long accessExpiration,
                         Collection<GrantedAuthority> authorities, Algorithm algorithm) {
    long now = System.currentTimeMillis();
    return JWT.create()
              .withSubject(username)
              .withExpiresAt(new Date(now +
                                      accessExpiration))
              .withIssuer(issuer)
              .withClaim("roles", authorities
                  .stream()
                  .map(GrantedAuthority::getAuthority)
                  .collect(Collectors.toList()))
              .sign(algorithm);
  }
}
