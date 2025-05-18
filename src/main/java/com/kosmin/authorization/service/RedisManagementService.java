package com.kosmin.authorization.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosmin.authorization.exception.RedisCacheException;
import com.kosmin.authorization.model.UserEntity;
import com.kosmin.authorization.repository.UserRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisManagementService {
  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper;
  private final UserRepository userRepository;

  public void cacheUser(UserEntity userEntity) {
    Optional.ofNullable(userRepository.findByUsername(userEntity.getUsername()))
        .ifPresentOrElse(
            user -> {
              String json;
              try {
                json = objectMapper.writeValueAsString(userEntity);
              } catch (JsonProcessingException e) {
                String errorMessage =
                    String.format(
                        "Error while serializing user: %s :: %s",
                        userEntity.getUsername(), e.getMessage());
                log.error(errorMessage);
                throw new RedisCacheException(errorMessage);
              }
              String key = "user:" + userEntity.getUsername();
              redisTemplate.opsForValue().set(key, json, Duration.ofMinutes(30));
            },
            () -> {
              String errorMessage =
                  String.format("User %s not found in SOT DB", userEntity.getUsername());
              log.error(errorMessage);
              throw new RedisCacheException(errorMessage);
            });
  }

  public Optional<UserEntity> getUser(String username) {
    String key = "user:" + username;
    String json = redisTemplate.opsForValue().get(key);
    if (json == null) return Optional.empty();

    try {
      UserEntity user = objectMapper.readValue(json, UserEntity.class);
      return Optional.of(user);
    } catch (JsonProcessingException e) {
      String errorMessage =
          String.format("Failed to deserialize user JSON for %s: %s", username, e.getMessage());
      log.error(errorMessage);
      throw new RedisCacheException(errorMessage);
    }
  }
}
