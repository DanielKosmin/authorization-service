package com.kosmin.authorization.validator;

import com.kosmin.authorization.config.PropertyConfig;
import com.kosmin.authorization.model.RegisterUser;
import com.kosmin.authorization.model.UserEntity;
import com.kosmin.authorization.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidator
    implements ConstraintValidator<ValidateRegistration, RegisterUser> {

  private final UserRepository userRepository;
  private final PropertyConfig propertyConfig;

  @Autowired
  public RegistrationValidator(UserRepository userRepository, PropertyConfig propertyConfig) {
    this.userRepository = userRepository;
    this.propertyConfig = propertyConfig;
  }

  @Override
  public boolean isValid(RegisterUser user, ConstraintValidatorContext context) {
    var r = propertyConfig.getSpecialChars();
    String username = user.getUsername();
    String password = user.getPassword();
    String validUsername = isValidUsername(username);
    String validPassword = isValidPassword(password);
    if (StringUtils.isNotBlank(validUsername)) addConstraintViolation(context, validUsername);
    if (StringUtils.isNotBlank(validPassword)) addConstraintViolation(context, validPassword);
    return StringUtils.isBlank(validUsername) && StringUtils.isBlank(validPassword);
  }

  private String isValidUsername(String username) {
    Optional<UserEntity> userEntity = userRepository.findByUsername(username);
    if (userEntity.isPresent()) {
      return "Duplicate username, please try again";
    }
    return "";
  }

  private String isValidPassword(String password) {
    boolean validLength = password.length() >= 12;
    boolean containsSpecialChar =
        Arrays.stream(propertyConfig.getSpecialChars().split(",")).anyMatch(password::contains);
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
      errorMessage.append(
          String.format(
              "at least one special character from: %s ", propertyConfig.getSpecialChars()));
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
