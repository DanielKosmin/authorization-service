package com.kosmin.authorization.validator;

import com.kosmin.authorization.model.TokenGenerationRequest;
import com.kosmin.authorization.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class TokenGenerationValidator
    implements ConstraintValidator<ValidateTokenGeneration, TokenGenerationRequest> {

  private final UserRepository userRepository;

  @Autowired
  public TokenGenerationValidator(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public boolean isValid(
      TokenGenerationRequest tokenGenerationRequest, ConstraintValidatorContext context) {
    boolean userPresent =
        Optional.ofNullable(userRepository.findByUsername(tokenGenerationRequest.getUsername()))
            .isPresent();
    if (userPresent) return true;
    context.disableDefaultConstraintViolation();
    context
        .buildConstraintViolationWithTemplate(
            "Username or password is incorrect or unregistered user.")
        .addConstraintViolation();
    return false;
  }
}
