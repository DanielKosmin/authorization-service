package com.kosmin.authorization.service;

import com.kosmin.authorization.model.Response;
import com.kosmin.authorization.model.TokenGenerationRequest;
import com.kosmin.authorization.model.UserEntity;
import io.micrometer.common.util.StringUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenManagementService {

  private final RedisManagementService redisManagementService;
  private final TokenProvisioningService tokenProvisioningService;

  public ResponseEntity<Response> retrieveToken(TokenGenerationRequest tokenGenerationRequest) {

    Optional<UserEntity> userEntity =
        redisManagementService.getUser(tokenGenerationRequest.getUsername());
    if (userEntity.isPresent()) {
      return ResponseEntity.ok().build();
    } else {
      Response response = tokenProvisioningService.provisionToken(tokenGenerationRequest);
      HttpStatus httpStatus =
          StringUtils.isNotBlank(response.getErrorMessage())
              ? HttpStatus.BAD_REQUEST
              : HttpStatus.CREATED;
      return ResponseEntity.status(httpStatus).body(response);
    }
  }
}
