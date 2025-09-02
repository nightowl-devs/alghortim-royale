package dev.nightowl.alghortimroyale.core;

import dev.nightowl.alghortimroyale.dns.DNSModule;
import dev.nightowl.alghortimroyale.module.Logger;
import dev.nightowl.alghortimroyale.module.Module;
import dev.nightowl.alghortimroyale.module.enums.LogLevel;
import dev.nightowl.alghortimroyale.proxy.ProxyModule;

import java.util.ArrayList;

public class App {
  private final ArrayList<Module> modules = new ArrayList<>();

  private static App instance;

  public App() {
      instance = this;
  }

  public static App getInstance() {
      return instance;
  }

  public void start() {
    modules.add(new DNSModule());
    modules.add(new ProxyModule());

    loadModules();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> this.shutdown(true)));
  }

  private void loadModules() {
    for (Module module : modules) {
      Logger.log(LogLevel.INFO, "Loading module: " + module.getName());
      module.init();
      module.running = true;
    }
  }

  private void unloadModules() {

      for (Module module : modules) {
          if(!module.running) return;
          Logger.log(LogLevel.INFO,"Unloading module: " + module.getName() );
          module.shutdown();
          module.running = false;
      }
  }



  public void shutdown(Boolean graceful) {
      unloadModules();

      int status =0;
      if (!graceful) {
          Logger.log(LogLevel.ERROR, "The application has encountered an error while running...");
          Logger.log(LogLevel.ERROR, "Read the above logs in order to resolve the issue!");
          status = 1;
      }

      Logger.log(LogLevel.INFO, "Goodbye...");
      System.exit(status);

  }



}
