package dev.nightowl.alghortimroyale.proxy;

import dev.nightowl.alghortimroyale.module.Logger;
import dev.nightowl.alghortimroyale.module.enums.LogLevel;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProxyServer {
    private final String REMOTE_HOST = "game.clashroyaleapp.com";
    private final Integer REMOTE_PORT = 9339;
    private ServerSocket serverSocket;

    public void start() {
        Logger.log(LogLevel.INFO, "Starting clash royale proxy server on port " + REMOTE_PORT);
        try {
            serverSocket = new ServerSocket(REMOTE_PORT);
            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                Logger.log(LogLevel.ERROR, "Error starting proxy server: " + e.getMessage());
            } else {
                Logger.log(LogLevel.INFO, "Proxy server stopped.");
            }
        }
    }

    public void stop() {
        Logger.log(LogLevel.INFO, "Stopping proxy server...");
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            Logger.log(LogLevel.ERROR, "Error stopping proxy server: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (Socket serverSocket = new Socket(REMOTE_HOST, REMOTE_PORT);
             InputStream clientInput = clientSocket.getInputStream();
             OutputStream clientOutput = clientSocket.getOutputStream();
             InputStream serverInput = serverSocket.getInputStream();
             OutputStream serverOutput = serverSocket.getOutputStream()) {

            Thread clientToServer = new Thread(() -> forwardTraffic(clientInput, serverOutput, "Client -> Server"));
            Thread serverToClient = new Thread(() -> forwardTraffic(serverInput, clientOutput, "Server -> Client"));

            clientToServer.start();
            serverToClient.start();

            clientToServer.join();
            serverToClient.join();
        } catch (IOException | InterruptedException e) {
            Logger.log(LogLevel.ERROR, "Connection error: " + e.getMessage());
        }
    }

    private void forwardTraffic(InputStream input, OutputStream output, String direction) {
        try (BufferedInputStream bufferedInput = new BufferedInputStream(input);
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(output)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bufferedInput.read(buffer)) != -1) {
                Logger.log(LogLevel.INFO, direction + " - Packet size: " + bytesRead + " bytes");
                bufferedOutput.write(buffer, 0, bytesRead);
                bufferedOutput.flush();
            }
        } catch (IOException e) {
            Logger.log(LogLevel.ERROR, "Error forwarding traffic: " + e.getMessage());
        }
    }
}
