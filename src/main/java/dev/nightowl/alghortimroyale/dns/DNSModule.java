package dev.nightowl.alghortimroyale.dns;

import dev.nightowl.alghortimroyale.core.App;
import dev.nightowl.alghortimroyale.module.Logger;
import dev.nightowl.alghortimroyale.module.Module;
import dev.nightowl.alghortimroyale.module.enums.LogLevel;
import dev.nightowl.alghortimroyale.core.util.NetworkUtil;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class DNSModule extends Module {
    private static final int DNS_PORT = 53;
    private static final String LISTEN_ADDRESS = "0.0.0.0";
    private static final String AUTHORITY_DNS = "1.1.1.1"; // cloduflare dns

    private DatagramSocket socket;
    private ExecutorService executor;
    private volatile boolean running = false;

    private static final List<DNSEntry> entries = Collections.singletonList(
        new DNSEntry(".*clashroyaleapp.*", "127.0.0.1") //game.clashroyaleapp.com : 9339 (it connects to this but why wont catch all for some extras i guess... hope this wont be a problem laterrr)
    );

    public void start() {
        try {
            String localAddress = NetworkUtil.getLocalAddress();

            socket = new DatagramSocket(DNS_PORT, InetAddress.getByName(LISTEN_ADDRESS));
            executor = Executors.newCachedThreadPool();
            running = true;

            Logger.log(LogLevel.INFO, "DNS Server is listening on: " + localAddress + ":" + DNS_PORT);
            Logger.log(LogLevel.INFO, "If you are on iPhone go to Settings > Wi-Fi > Your Network > Configure DNS > Manual and add " + localAddress + " as your DNS server");
            Logger.log(LogLevel.INFO, "Make sure to remove any other DNS servers temporarily, and TURN OFF PRIVATE RELAY if you have it enabled");

            executor.submit(this::listenForQueries);

        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "Failed to start DNS server: \n" + e.getMessage());
            Logger.log(LogLevel.ERROR, "Attempted to bind to " + LISTEN_ADDRESS + ":" + DNS_PORT);
            Logger.log(LogLevel.ERROR, "Make sure to run the program as administrator/root to bind to port 53");
            stop();
            App.getInstance().shutdown(false);

        }
    }

    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        if (executor != null) {
            executor.shutdown();
        }
        Logger.log(LogLevel.WARN, "DNS Server closed");
    }

    private void listenForQueries() {
        byte[] buffer = new byte[512];

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                executor.submit(() -> handleRequest(packet));

            } catch (SocketException e) {
                if (running) {
                    Logger.log(LogLevel.ERROR, "Socket error: " + e.getMessage());
                }
            } catch (IOException e) {
                Logger.log(LogLevel.ERROR, "IO error: " + e.getMessage());
            }
        }
    }

    private void handleRequest(DatagramPacket packet) {
        try {
            byte[] packetData = packet.getData();
            if (packetData == null || packetData.length == 0) {
                Logger.log(LogLevel.ERROR, "Received empty or null packet data.");
                return;
            }

            Message request;
            try {
                request = new Message(packetData);
            } catch (WireParseException e) {
                Logger.log(LogLevel.ERROR, "Failed to parse DNS request: " + e.getMessage());
                return;
            }

            Message response = new Message(request.getHeader().getID());

            response.getHeader().setFlag(Flags.QR);
            response.getHeader().setFlag(Flags.RA);

            List<Record> questions = request.getSection(Section.QUESTION);
            List<Record> answers = new ArrayList<>();

            for (Record question : questions) {
                if (question != null) {
                    DNSEntry matchingEntry = findMatchingEntry(question.getName().toString());
                    if (matchingEntry != null) {
                        Logger.log(LogLevel.INFO, "faking dns record for: " + question.getName() + " resolving to:  " + matchingEntry.address);
                        ARecord spoofedRecord = new ARecord(question.getName(), DClass.IN, 1800, InetAddress.getByName(matchingEntry.address));
                        answers.add(spoofedRecord);
                    } else {
                        List<Record> proxiedAnswers = proxyRequest(question);
                        answers.addAll(proxiedAnswers);
                    }
                }
            }

            for (Record answer : answers) {
                response.addRecord(answer, Section.ANSWER);
            }

            byte[] responseData = response.toWire();
            DatagramPacket responsePacket = new DatagramPacket(
                responseData, responseData.length,
                packet.getAddress(), packet.getPort()
            );
            socket.send(responsePacket);

        } catch (Exception e) {
            Logger.log(LogLevel.ERROR, "Error handling DNS request: " + e.getMessage());
        }
    }

    private DNSEntry findMatchingEntry(String domainName) {
        for (DNSEntry entry : entries) {
            if (Pattern.matches(entry.domainPattern, domainName)) {
                return entry;
            }
        }
        return null;
    }

    private List<Record> proxyRequest(Record question) {
        List<Record> answers = new ArrayList<>();

        try {
            Message query = Message.newQuery(question);
            SimpleResolver resolver = new SimpleResolver(AUTHORITY_DNS);

            Message response = resolver.send(query);

            List<Record> answerRecords = response.getSection(Section.ANSWER);
            answers.addAll(answerRecords);

        } catch (Exception e) {
            Logger.log(LogLevel.WARN, "Failed to proxy DNS request for " + question.getName() + ": " + e.getMessage());
        }

        return answers;
    }

    @Override
    public void init() {
        this.start();

    }

    @Override
    public void shutdown() {
        this.stop();

    }

    @Override
    public String getName() {
        return "DNS Server";
    }


    private static class DNSEntry {
        final String domainPattern;
        final String address;

        DNSEntry(String domainPattern, String address) {
            this.domainPattern = domainPattern;
            this.address = address;
        }
    }
}
