package dev.nightowl.module;

public abstract class Module {
  public abstract void init();

  public abstract void shutdown();

  public abstract String getName();

}
