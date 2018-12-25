package com.genetic.bots.BotsHandling;

public class Gene {
    private byte value; // Will be in range [0; 63]

    public Gene(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
