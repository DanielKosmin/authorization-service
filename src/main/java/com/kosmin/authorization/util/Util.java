package com.kosmin.authorization.util;

import java.time.Instant;

public class Util {

  public static long computeCurrentUnixTimestampMilliseconds() {
    Instant now = java.time.Instant.now();
    return now.toEpochMilli();
  }
}
