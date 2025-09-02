package dev.nightowl.alghortimroyale.module;

public abstract class Module {
  public abstract void init();

  public boolean running = false;

  public abstract void shutdown();

  public abstract String getName();

}
