package com.matejkala.models;

public enum RoleType {
    ADMIN("ADMIN"),
    USER("USER"),
    GUEST("GUEST");

    private final String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
