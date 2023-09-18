package com.gfa.services;

import static java.util.Arrays.stream;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${token.expiration.access}")
  private long accessExp;
  @Value("${token.expiration.refresh}")
  private long refreshExp;
  @Value("${token.secret}")
  private String secret;

  @NotNull
  private static Collection<? extends GrantedAuthority> getGrantedAuthorities(
      DecodedJWT decodedJwt) {
    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
    String[] roles = decodedJwt.getClaim("roles")
                               .asArray(String.class);
    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
    return authorities;
  }

  public Algorithm getAlgorithm() {
    return Algorithm.HMAC256(secret);
  }

  public TokenServiceImpl() {
  }

  public JWTVerifier getVerifier() {
    return JWT.require(getAlgorithm())
              .build();
  }

  @NotNull
  @Override
  public UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
    DecodedJWT decodedJwt = getVerifier().verify(token);
    return new UsernamePasswordAuthenticationToken(decodedJwt.getSubject(),
                                                   null,
                                                   getGrantedAuthorities(decodedJwt));
  }

  @Override
  public String getToken(String username, Calendar now, String issuer) {
    return JWT.create()
              .withSubject(username)
              .withExpiresAt(new Date(now.getTimeInMillis() + refreshExp))
              .withIssuer(issuer)
              .sign(getAlgorithm());
  }

  @Override
  public String getToken(String username, Calendar now, String issuer,
                         Collection<GrantedAuthority> authorities) {
    return JWT.create()
              .withSubject(username)
              .withExpiresAt(new Date(now.getTimeInMillis() + accessExp))
              .withIssuer(issuer)
              .withClaim("roles", authorities
                  .stream()
                  .map(GrantedAuthority::getAuthority)
                  .collect(Collectors.toList()))
              .sign(getAlgorithm());
  }
}
