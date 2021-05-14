package com.model;

public abstract class Thing {
    public abstract String toScs();
    public abstract String getName();

    public static Thing fromString(String json) {
        return null;
    }
}
