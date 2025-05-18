package com.kosmin.authorization.validator;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.kosmin.authorization.model.RegisterUser;
import com.kosmin.authorization.model.UserEntity;
import com.kosmin.authorization.repository.UserRepository;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class RegistrationValidatorTest {

  @Mock private UserRepository userRepository;
  @Mock private ConstraintValidatorContext context;
  @Mock private ConstraintValidatorContext.ConstraintViolationBuilder builder;

  private RegistrationValidator registrationValidator;

  String username = "username";

  @BeforeEach
  void setUp() {
    lenient().when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(builder);
    Properties properties = loadYamlProperties();
    String specialChars = properties.getProperty("authorization.service.special.chars");
    registrationValidator = new RegistrationValidator(userRepository, specialChars);
  }

  @Test
  public void shouldReturnFalse_whenUsernameIsAvailableAndPasswordIsInvalid() {
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    Assertions.assertFalse(
        registrationValidator.isValid(
            RegisterUser.builder().username(username).password("dqwd").build(), context));
  }

  @Test
  public void shouldReturnFalse_whenUsernameIsNotAvailableAndPasswordIsInvalid() {
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));
    Assertions.assertFalse(
        registrationValidator.isValid(
            RegisterUser.builder().username(username).password("dqwd").build(), context));
  }

  @Test
  public void shouldReturnFalse_whenUsernameIsNotAvailableAndPasswordIsValid() {
    when(userRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));
    Assertions.assertFalse(
        registrationValidator.isValid(
            RegisterUser.builder().username(username).password("dqwd1X@cqwmqw").build(), context));
  }

  @Test
  public void shouldReturnTrue_whenUsernameIsAvailableAndPasswordIsValid() {
    when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
    Assertions.assertTrue(
        registrationValidator.isValid(
            RegisterUser.builder().username(username).password("dqwd1X@cqwmqw").build(), context));
  }

  private Properties loadYamlProperties() {
    final Resource resource = new ClassPathResource("application.yml");
    final YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
    yamlPropertiesFactoryBean.setResources(resource);
    return yamlPropertiesFactoryBean.getObject();
  }
}
