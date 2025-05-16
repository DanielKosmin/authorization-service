package com.kosmin.authorization.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public String hashPassword(String password) {
    return bCryptPasswordEncoder.encode(password);
  }

  public boolean validatePassword(String password, String hashedPassword) {
    return bCryptPasswordEncoder.matches(password, hashedPassword);
  }
}
