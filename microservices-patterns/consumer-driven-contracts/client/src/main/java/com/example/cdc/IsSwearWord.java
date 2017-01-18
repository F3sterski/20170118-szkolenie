package com.example.cdc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IsSwearWord {

    final public boolean containsProfanity;
    final public String input;
    final public String output;

    public IsSwearWord(@JsonProperty("containsProfanity") boolean containsProfanity,
                       @JsonProperty("input") String input,
                       @JsonProperty("output") String output) {
        this.containsProfanity = containsProfanity;
        this.input = input;
        this.output = output;
    }

    @Override
    public String toString() {
        return "IsSwearWord{" +
                "containsProfanity=" + containsProfanity +
                ", input='" + input + '\'' +
                ", output='" + output + '\'' +
                '}';
    }
}
