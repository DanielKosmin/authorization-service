package com.kosmin.authorization.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@RefreshScope
@Component
public class PropertyConfig {

  @Value("${authorization.service.special.chars}")
  private String specialChars;
}
