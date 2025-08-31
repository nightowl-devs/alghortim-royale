package dev.nightowl.alghortimroyale.network;


import dev.nightowl.alghoritmroyale.shared.module.Module;
import dev.nightowl.alghoritmroyale.shared.util.Logger;
import dev.nightowl.alghoritmroyale.shared.util.enums.LogLevel;

public class NetworkingModule extends Module {



    @Override
    public void init() {
        Logger.log(LogLevel.DEBUG,"loaded networking module");

    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return "Networking";
    }
}