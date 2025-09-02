package dev.nightowl.alghortimroyale.core;

import dev.nightowl.alghortimroyale.dns.DNSModule;
import dev.nightowl.alghortimroyale.module.Logger;
import dev.nightowl.alghortimroyale.module.Module;
import dev.nightowl.alghortimroyale.module.enums.LogLevel;
import dev.nightowl.alghortimroyale.proxy.ProxyModule;

import java.util.ArrayList;

public class App {
  private final ArrayList<Module> modules = new ArrayList<>();

  public void start() {
    modules.add(new DNSModule());
    modules.add(new ProxyModule());

    loadModules();
  }

  private void loadModules() {
    for (Module module : modules) {
      Logger.log(LogLevel.INFO, "Loading module: " + module.getName());
      module.init();
    }
  }
}
