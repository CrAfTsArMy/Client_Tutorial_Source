package de.craftsarmy.discord;

import de.craftsarmy.Variables;

import java.util.UUID;

public class RPCParty {

    private final String id;
    private final String joinsecret;
    private int size;
    private int max;


    public RPCParty(int max) {
        this.id = UUID.randomUUID().toString();
        this.joinsecret = UUID.randomUUID().toString().substring(0, 28).replace("-", "").toUpperCase();
        this.size = 1;
        this.max = max;
    }

    public void addSize() {
        if (size < max)
            this.size += 1;
    }

    public void removeSize() {
        if (size >= 1)
            this.size -= 1;
        if (size == 0)
            Variables.rpc.destroyParty();
    }

    public void setSize(int size) {
        if (size <= max)
            this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return max;
    }

    public String getId() {
        return id;
    }

    public String getJoinsecret() {
        return joinsecret;
    }

}
