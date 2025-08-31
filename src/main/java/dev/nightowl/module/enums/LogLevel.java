package dev.nightowl.module.enums;

public enum LogLevel {
  INFO(Color.GREEN),
  WARN(Color.YELLOW),
  ERROR(Color.RED),
  DEBUG(Color.CYAN);

  private final Color color;

  LogLevel(Color color) {
    this.color = color;
  }

  public String color() {
    return color.toString();
  }
}
