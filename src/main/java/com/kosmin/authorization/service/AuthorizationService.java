package com.kosmin.authorization.service;

import com.kosmin.authorization.model.RegisterUser;
import com.kosmin.authorization.model.Response;
import com.kosmin.authorization.model.Status;
import com.kosmin.authorization.model.TokenGenerationRequest;
import com.kosmin.authorization.model.UserEntity;
import com.kosmin.authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

  private final UserRepository userRepository;
  private final PasswordService passwordService;
  private final TokenManagementService tokenManagementService;

  public ResponseEntity<Response> registerUser(RegisterUser entity) {
    try {
      userRepository.save(
          UserEntity.builder()
              .username(entity.getUsername())
              .password(passwordService.hashPassword(entity.getPassword()))
              .build());
      return ResponseEntity.status(HttpStatus.CREATED)
          .body(
              Response.builder()
                  .message("User Registered Successfully")
                  .status(Status.SUCCESS)
                  .build());
    } catch (Exception e) {
      log.error("Error while saving user", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Response.builder().errorMessage(e.getMessage()).status(Status.FAILURE).build());
    }
  }

  public ResponseEntity<Response> generateToken(TokenGenerationRequest generationCredentials) {
    return tokenManagementService.retrieveToken(generationCredentials);
  }
}
