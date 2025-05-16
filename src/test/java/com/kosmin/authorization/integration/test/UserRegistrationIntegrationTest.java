package com.kosmin.authorization.integration.test;

import com.kosmin.authorization.model.RegisterEntity;
import com.kosmin.authorization.model.RegisterUser;
import com.kosmin.authorization.model.Response;
import com.kosmin.authorization.model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class UserRegistrationIntegrationTest extends BaseIntegrationTest {

  @Test
  public void registerUserWithValidCredentials() {
    String username = "testusername";
    Response response =
        sendHttpReq(
            HttpMethod.POST,
            "authorization/v1/register",
            RegisterUser.builder().username(username).password("1@rR2cce22sdvv").build(),
            Response.class,
            HttpStatus.CREATED);
    Assertions.assertEquals(Status.SUCCESS, response.getStatus());
    RegisterEntity entity = userRepository.findByUsername(username);
    Assertions.assertNotNull(entity);
  }

  @Test
  public void registerUserWithInvalidCredentials() {
    Response response =
        sendHttpReq(
            HttpMethod.POST,
            "authorization/v1/register",
            RegisterUser.builder().username("username").password("password").build(),
            Response.class,
            HttpStatus.BAD_REQUEST);
    Assertions.assertFalse(response.getValidationErrors().isEmpty());
  }

  @Test
  public void registerDuplicateUser() {
    String username = "testusername6";
    String password = "1@rR2cce22sdvv";
    Response response =
        sendHttpReq(
            HttpMethod.POST,
            "authorization/v1/register",
            RegisterUser.builder().username(username).password(password).build(),
            Response.class,
            HttpStatus.CREATED);
    Assertions.assertEquals(Status.SUCCESS, response.getStatus());
    RegisterEntity entity = userRepository.findByUsername(username);
    Assertions.assertNotNull(entity);

    Response dupRes =
        sendHttpReq(
            HttpMethod.POST,
            "authorization/v1/register",
            RegisterUser.builder().username(username).password(password).build(),
            Response.class,
            HttpStatus.BAD_REQUEST);
    Assertions.assertEquals(Status.FAILURE, dupRes.getStatus());
    Assertions.assertTrue(
        dupRes.getValidationErrors().stream()
            .anyMatch(v -> v.contains("Duplicate username, please try again")));
  }
}
