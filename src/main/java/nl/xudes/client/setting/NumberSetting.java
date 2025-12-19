package nl.xudes.client.setting;

public class NumberSetting extends Setting {
    private double value;
    private final double min;
    private final double max;
    private final double increment;

    public NumberSetting(String name, double defaultValue, double min, double max, double increment) {
        super(name);
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getValue() {
        return value;
    }

    public float getValueFloat() {
        return (float) value;
    }

    public int getValueInt() {
        return (int) value;
    }

    public void setValue(double value) {
        value = Math.max(min, Math.min(max, value));
        this.value = Math.round(value * (1.0 / increment)) / (1.0 / increment);
    }

    public double getMin() { return min; }
    public double getMax() { return max; }
}