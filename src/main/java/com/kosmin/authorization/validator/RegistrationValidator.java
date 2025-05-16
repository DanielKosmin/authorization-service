package com.kosmin.authorization.validator;

import com.kosmin.authorization.model.RegisterEntity;
import com.kosmin.authorization.model.RegisterUser;
import com.kosmin.authorization.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidator
    implements ConstraintValidator<ValidateRegistration, RegisterUser> {

  private final UserRepository userRepository;
  private final String specialChars;

  @Autowired
  public RegistrationValidator(
      UserRepository userRepository,
      @Value("${authorization.service.special.chars}") String specialChars) {
    this.userRepository = userRepository;
    this.specialChars = specialChars;
  }

  @Override
  public boolean isValid(RegisterUser user, ConstraintValidatorContext context) {
    String username = user.getUsername();
    String password = user.getPassword();
    String validUsername = isValidUsername(username);
    String validPassword = isValidPassword(password);
    if (StringUtils.isNotBlank(validUsername)) addConstraintViolation(context, validUsername);
    if (StringUtils.isNotBlank(validPassword)) addConstraintViolation(context, validPassword);
    return StringUtils.isBlank(validUsername) && StringUtils.isBlank(validPassword);
  }

  private String isValidUsername(String username) {
    RegisterEntity registerEntity = userRepository.findByUsername(username);
    if (registerEntity != null) {
      return "Duplicate username, please try again";
    }
    return "";
  }

  private String isValidPassword(String password) {
    boolean validLength = password.length() >= 12;
    boolean containsSpecialChar =
        Arrays.stream(specialChars.split(",")).anyMatch(password::contains);
    boolean containsUppercase = password.chars().anyMatch(Character::isUpperCase);
    boolean containsLowercase = password.chars().anyMatch(Character::isLowerCase);
    boolean containsNumber = password.chars().anyMatch(Character::isDigit);
    if (validLength
        && containsSpecialChar
        && containsUppercase
        && containsLowercase
        && containsNumber) return "";

    StringBuilder errorMessage = new StringBuilder("Password must contain ");
    if (!validLength) errorMessage.append("at least 12 characters, ");
    if (!containsSpecialChar)
      errorMessage.append(String.format("at least one special character from: %s ", specialChars));
    if (!containsUppercase) errorMessage.append("at least one uppercase letter, ");
    if (!containsLowercase) errorMessage.append("at least one lowercase letter, ");
    if (!containsNumber) errorMessage.append("at least one number, ");

    if (errorMessage.toString().endsWith(", ")) {
      errorMessage.setLength(errorMessage.length() - 2);
    }
    return errorMessage.toString();
  }

  private void addConstraintViolation(ConstraintValidatorContext context, String violation) {
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(violation).addConstraintViolation();
  }
}
