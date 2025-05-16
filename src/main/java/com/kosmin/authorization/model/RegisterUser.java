package com.kosmin.authorization.model;

import com.kosmin.authorization.validator.ValidateRegistration;
import lombok.Builder;
import lombok.Data;

@Data
@ValidateRegistration
@Builder(toBuilder = true)
public class RegisterUser {
  private String username;
  private String password;
}
