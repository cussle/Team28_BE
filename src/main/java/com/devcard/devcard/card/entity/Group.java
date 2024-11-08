package com.devcard.devcard.card.entity;

public class Group {
    private String name;
    private int count;

    public Group(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
