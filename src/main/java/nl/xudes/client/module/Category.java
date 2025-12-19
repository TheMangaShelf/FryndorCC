package nl.xudes.client.module;

public enum Category {
    COMBAT("Combat"),
    UTILITIES("Utilities");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}