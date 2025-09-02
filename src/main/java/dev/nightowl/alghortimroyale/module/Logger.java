package dev.nightowl.alghortimroyale.module;

import dev.nightowl.alghortimroyale.module.enums.Color;
import dev.nightowl.alghortimroyale.module.enums.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
  //format
  // gray [ color LEVEL gray ] white message resetColor
  public static void log(LogLevel level, String message) {
    String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    System.out.println(Color.GRAY + "[" + Color.RESET + Color.BLUE + time + Color.GRAY + " " + level.color() + level + Color.GRAY + "] " + Color.RESET + message + Color.RESET);
  }
}
