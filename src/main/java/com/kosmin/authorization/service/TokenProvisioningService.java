package com.kosmin.authorization.service;

import static com.kosmin.authorization.util.Util.computeCurrentUnixTimestampMilliseconds;

import com.kosmin.authorization.model.Response;
import com.kosmin.authorization.model.Status;
import com.kosmin.authorization.model.TokenGenerationRequest;
import com.kosmin.authorization.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenProvisioningService {

  private final UserRepository userRepository;
  private final PasswordService passwordService;
  private final Key jwtSecret;

  public Response provisionToken(TokenGenerationRequest tokenGenerationRequest) {
    long currentUnixTime = computeCurrentUnixTimestampMilliseconds();
    // set token expiration to one hour
    long expirationTime = currentUnixTime + 3600000L;

    Response.ResponseBuilder response = Response.builder();
    userRepository
        .findByUsername(tokenGenerationRequest.getUsername())
        .ifPresentOrElse(
            user -> {
              boolean validPassword =
                  passwordService.validatePassword(
                      tokenGenerationRequest.getPassword(), user.getPassword());
              if (validPassword) {
                String jwtToken =
                    generateToken(
                        tokenGenerationRequest.getUsername(), currentUnixTime, expirationTime);
                user.setTokenExpiration(expirationTime);
                Thread.startVirtualThread(() -> userRepository.save(user));
                response
                    .token(jwtToken)
                    .status(Status.SUCCESS)
                    .message("Successfully provisioned token");
              } else {
                response.status(Status.FAILURE).errorMessage("Invalid password");
              }
            },
            () ->
                response
                    .status(Status.FAILURE)
                    .errorMessage("incorrect username or user not registered"));
    return response.build();
  }

  private String generateToken(String username, long currentUnixTime, long expirationTime) {
    return Jwts.builder()
        .claims(Map.of("sub", username))
        .issuedAt(new Date(currentUnixTime))
        .expiration(new Date(expirationTime))
        .signWith(jwtSecret)
        .compact();
  }
}
