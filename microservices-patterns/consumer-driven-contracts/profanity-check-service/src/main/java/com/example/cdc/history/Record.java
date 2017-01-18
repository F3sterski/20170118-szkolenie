package com.example.cdc.history;

import java.io.Serializable;
import java.time.Instant;

public class Record implements Serializable {

    final public String input;
    final public Instant time;

    public Record(String input, Instant time) {
        this.input = input;
        this.time = time;
    }

    @Override
    public String toString() {
        return "Record{" +
                "input='" + input + '\'' +
                ", time=" + time +
                '}';
    }
}
