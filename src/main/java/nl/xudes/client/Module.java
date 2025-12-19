package nl.xudes.client;

public class Module {
    private String name;
    private boolean enabled;

    public Module(String name) {
        this.name = name;
        this.enabled = false;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }
}