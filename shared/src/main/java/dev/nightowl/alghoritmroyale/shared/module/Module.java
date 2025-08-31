package dev.nightowl.alghoritmroyale.shared.module;

public abstract class Module {
    public abstract void init();
    public abstract void shutdown();

    public abstract String getName();

}
