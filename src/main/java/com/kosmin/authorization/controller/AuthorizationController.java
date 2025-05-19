package com.kosmin.authorization.controller;

import com.kosmin.authorization.model.RegisterUser;
import com.kosmin.authorization.model.Response;
import com.kosmin.authorization.model.TokenGenerationRequest;
import com.kosmin.authorization.service.AuthorizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("authorization/v1")
@RestController
@Validated
@RequiredArgsConstructor
public class AuthorizationController {

  private final AuthorizationService authorizationService;

  @PostMapping("register")
  public ResponseEntity<Response> registerUser(@Valid @RequestBody RegisterUser entity) {
    return authorizationService.registerUser(entity);
  }

  @PostMapping("token")
  public ResponseEntity<Response> generateToken(
      @Valid @RequestBody TokenGenerationRequest tokenGenerationRequest) {
    return authorizationService.retrieveToken(tokenGenerationRequest);
  }

  @GetMapping("health")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Health Check OK");
  }
}
