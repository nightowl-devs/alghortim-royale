package dev.nightowl.alghortimroyale.core;

import dev.nightowl.alghoritmroyale.shared.module.Module;
import dev.nightowl.alghoritmroyale.shared.util.Logger;
import dev.nightowl.alghoritmroyale.shared.util.enums.LogLevel;
import dev.nightowl.alghortimroyale.network.NetworkingModule;

import java.util.ArrayList;

public class App {
    private final ArrayList<Module> modules = new ArrayList<>();

    public  void start() {
        modules.add(new NetworkingModule());

        loadModules();



    }

    private  void loadModules() {
        for (Module module : modules) {
            Logger.log(LogLevel.INFO, "Loading module: " + module.getName());
            module.init();
        }
    }
}