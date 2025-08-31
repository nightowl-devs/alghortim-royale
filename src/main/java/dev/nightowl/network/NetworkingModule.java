package dev.nightowl.network;

import dev.nightowl.module.Logger;
import dev.nightowl.module.Module;
import dev.nightowl.module.enums.LogLevel;

public class NetworkingModule extends Module {
    @Override
    public void init() {
        Logger.log(LogLevel.DEBUG, "loaded networking module");

    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getName() {
        return "Networking";
    }
}
