package dev.nightowl.core;

import dev.nightowl.module.Logger;
import dev.nightowl.module.Module;
import dev.nightowl.module.enums.LogLevel;
import dev.nightowl.network.NetworkingModule;

import java.util.ArrayList;

public class App {
  private final ArrayList<Module> modules = new ArrayList<>();

  public void start() {
    modules.add(new NetworkingModule());

    loadModules();
  }

  private void loadModules() {
    for (Module module : modules) {
      Logger.log(LogLevel.INFO, "Loading module: " + module.getName());
      module.init();
    }
  }
}
