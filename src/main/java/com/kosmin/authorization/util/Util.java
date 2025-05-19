package com.kosmin.authorization.util;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Util {

  public static long computeCurrentUnixTimestampMilliseconds() {
    Instant now = java.time.Instant.now();
    return now.toEpochMilli();
  }

  public static String convertUnixToISO8601(long unixTimestamp) {
    return Instant.ofEpochMilli(unixTimestamp)
        .atOffset(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_INSTANT);
  }
}
