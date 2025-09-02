package dev.nightowl.alghortimroyale.proxy;

import dev.nightowl.alghortimroyale.module.Module;

public class ProxyModule  extends Module {

    ProxyServer proxyServer;
    @Override
    public void init() {
        proxyServer = new ProxyServer();
        proxyServer.start();


    }

    @Override
    public void shutdown() {
        proxyServer.stop();


    }

    @Override
    public String getName() {
        return "Proxy Server";
    }
}
