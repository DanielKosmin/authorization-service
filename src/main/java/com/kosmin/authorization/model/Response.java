package com.kosmin.authorization.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
  private List<String> validationErrors;
  private String errorMessage;
  private String message;
  private Status status;
  private String token;
  private Long expiresAt;
  private String expiresAtIso;
}
