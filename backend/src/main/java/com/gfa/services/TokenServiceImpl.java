package com.gfa.services;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gfa.dtos.requestdtos.RequestTokenDTO;
import com.gfa.dtos.responsedtos.ResponseTokensDTO;
import com.gfa.exceptions.InvalidTokenException;
import com.gfa.exceptions.MissingBearerTokenException;
import com.gfa.models.AppUser;
import com.gfa.models.Role;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${token.expiration.access}")
  private Long accessExp;
  @Value("${token.expiration.refresh}")
  private Long refreshExp;
  @Value("${token.secret}")
  private String secret;
  private static final String PREFIX = "Bearer ";
  private final AppUserService appUserService;

  @Autowired
  public TokenServiceImpl(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  public String getPrefix() {
    return PREFIX;
  }

  @NotNull
  @Override
  public Set<SimpleGrantedAuthority> getGrantedAuthorities(String token) {
    DecodedJWT decodedJwt = decodeToken(token);
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    String[] roles = decodedJwt.getClaim("roles")
                               .asArray(String.class);
    stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
    return authorities;
  }

  @Override
  public Algorithm getAlgorithm() {
    return Algorithm.HMAC256(secret);
  }

  @Override
  public JWTVerifier getVerifier() {
    return JWT.require(getAlgorithm())
              .build();
  }

  @Override
  public String getSubject(String token) {
    return decodeToken(token).getSubject();
  }

  @Override
  public DecodedJWT decodeToken(String token) {
    return getVerifier().verify(token);
  }

  @Override
  public String createRefreshToken(String username, Calendar now, String issuer) {
    return JWT.create()
              .withSubject(username)
              .withExpiresAt(new Date(now.getTimeInMillis() + refreshExp))
              .withIssuer(issuer)
              .sign(getAlgorithm());
  }

  @Override
  public <T extends GrantedAuthority> String createAccessToken(String username, Calendar now,
                                                               String issuer,
                                                               Collection<T> authorities) {
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

  @Override
  public RequestTokenDTO mapToDto(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    if (authorizationHeader == null || !authorizationHeader.startsWith(PREFIX)) {
      throw new MissingBearerTokenException();
    }

    String token = authorizationHeader.substring(PREFIX.length());
    String issuer = request.getRequestURL()
                           .toString();

    return new RequestTokenDTO(token, issuer);
  }

  @Override
  public ResponseTokensDTO refreshTokens(RequestTokenDTO tokenDto) {

    AppUser appUser = appUserService.getAppUser(getSubject(tokenDto.getToken()));
    String username = appUser.getUsername();
    String issuer = tokenDto.getIssuer();
    Set<Role> authorities = appUser.getRoles();
    Calendar now = Calendar.getInstance();

    String accessToken = createAccessToken(username, now, issuer, authorities);
    String refresh_token = createRefreshToken(username, now, issuer);

    return mapToDto(accessToken, refresh_token);
  }

  @Override
  public Authentication getAuthentication(RequestTokenDTO requestTokenDTO) {
    String token = requestTokenDTO.getToken();
    try {
      return new UsernamePasswordAuthenticationToken(getSubject(token),
                                                     null,
                                                     getGrantedAuthorities(token));
    } catch (Exception e) {
      throw new InvalidTokenException();
    }
  }

  @Override
  public ResponseTokensDTO mapToDto(String accessToken, String refresh_token) {
    return new ResponseTokensDTO(accessToken, refresh_token);
  }
}
