package dev.nightowl.alghortimroyale.protocol;

import java.nio.Buffer;

public class Packet {
    public Integer id;
    public Integer length;
    public Integer version;
    public Buffer payload;

    public Packet(Integer id, Integer length, Integer version, Buffer payload) {
        this.id = id;
        this.length = length;
        this.version = version;
        this.payload = payload;
    }


}
