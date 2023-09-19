package com.gfa.services;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gfa.dtos.requestdtos.RequestTokenDTO;
import com.gfa.dtos.responsedtos.AccessResponseTokenDTO;
import com.gfa.dtos.responsedtos.RefreshResponseTokenDTO;
import com.gfa.dtos.responsedtos.ResponseTokenDTO;
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

  private final String prefix = "Bearer ";
  private final AppUserService appUserService;

  @Autowired
  public TokenServiceImpl(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @Override
  public String getPrefix() {
    return prefix;
  }

  @NotNull
  @Override
  public Set<SimpleGrantedAuthority> getGrantedAuthorities(DecodedJWT decodedJwt) {
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

  @NotNull
  @Override
  public UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
    DecodedJWT decodedJwt = decodeJwt(token);
    return new UsernamePasswordAuthenticationToken(decodedJwt.getSubject(),
                                                   null,
                                                   getGrantedAuthorities(decodedJwt));
  }

  @Override
  public String getSubject(String jwt) {
    return decodeJwt(jwt).getSubject();
  }

  @Override
  public DecodedJWT decodeJwt(String jwt) {
    return getVerifier().verify(jwt);
  }

  @Override
  public String createToken(String username, Calendar now, String issuer) {
    return JWT.create()
              .withSubject(username)
              .withExpiresAt(new Date(now.getTimeInMillis() + refreshExp))
              .withIssuer(issuer)
              .sign(getAlgorithm());
  }

  @Override
  public <T extends GrantedAuthority> String createToken(String username, Calendar now,
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
  public RequestTokenDTO parse(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(AUTHORIZATION);

    if (authorizationHeader == null || !authorizationHeader.startsWith(prefix)) {
      throw new RuntimeException("Refresh token is missing");
    }

    String token = authorizationHeader.substring(prefix.length());
    String username = getSubject(token);
    String issuer = request.getRequestURL()
                           .toString();

    return new RequestTokenDTO(token, username, issuer);
  }

  @Override
  public Set<ResponseTokenDTO> refreshTokens(RequestTokenDTO tokenDto) {

    AppUser appUser = appUserService.getAppUser(tokenDto.getSubject());
    String username = appUser.getUsername();
    String issuer = tokenDto.getIssuer();
    Set<Role> authorities = appUser.getRoles();
    Calendar now = Calendar.getInstance();

    String accessToken = createToken(username, now, issuer, authorities);
    AccessResponseTokenDTO accessTokenDto = new AccessResponseTokenDTO(accessToken);

    String refresh_token = createToken(username, now, issuer);
    RefreshResponseTokenDTO refreshTokenDto = new RefreshResponseTokenDTO(refresh_token);
    // TODO wrap in some responseDto
    Set<ResponseTokenDTO> tokensx = new HashSet<>();
    tokensx.add(accessTokenDto);
    return tokensx;
  }
}
