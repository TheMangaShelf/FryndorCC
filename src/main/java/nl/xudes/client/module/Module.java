package nl.xudes.client.module;

import net.minecraft.client.MinecraftClient;

public abstract class Module {
    protected final MinecraftClient mc = MinecraftClient.getInstance();
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public void toggle() {
        if (this.enabled) disable();
        else enable();
    }

    public void enable() {
        this.enabled = true;
        onEnable();
    }

    public void disable() {
        this.enabled = false;
        onDisable();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public void onEnable() {}
    public void onDisable() {}
    public void onTick() {}
}