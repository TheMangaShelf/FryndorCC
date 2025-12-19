package nl.xudes.client.setting;

public abstract class Setting {
    protected String name;
    protected boolean visible = true;

    public Setting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visible;
    }
}