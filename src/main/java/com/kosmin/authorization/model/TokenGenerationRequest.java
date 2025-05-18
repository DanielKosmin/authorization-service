package com.kosmin.authorization.model;

import com.kosmin.authorization.validator.ValidateTokenGeneration;
import lombok.Data;

@Data
@ValidateTokenGeneration
public class TokenGenerationRequest {

  private String username;
  private String password;
}
