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
    String username = "testusername6";
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
}
