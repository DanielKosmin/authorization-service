package com.kosmin.authorization.exception;

public class RedisCacheException extends RuntimeException {
  public RedisCacheException(String message) {
    super(message);
  }
}
