package com.kosmin.authorization.integration.test;

import com.kosmin.authorization.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "36000")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

  @Autowired protected WebTestClient webClient;
  @Autowired protected UserRepository userRepository;

  @BeforeAll
  public void beforeAll() {
    userRepository.deleteAll();
  }

  @AfterAll
  public void afterAll() {
    userRepository.deleteAll();
  }

  protected <T> T sendHttpReq(
      HttpMethod method,
      String uri,
      Object body,
      Class<T> responseType,
      HttpStatus expectedStatus) {

    WebTestClient.RequestBodySpec requestBodySpec = webClient.method(method).uri(uri);

    if (body != null) {
      requestBodySpec.bodyValue(body);
    }

    return requestBodySpec
        .exchange()
        .expectStatus()
        .isEqualTo(expectedStatus)
        .expectBody(responseType)
        .returnResult()
        .getResponseBody();
  }
}
