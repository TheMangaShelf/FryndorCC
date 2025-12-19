package nl.xudes.client.module.util;

import nl.xudes.client.module.Category;
import nl.xudes.client.module.Module;

public class Fullbright extends Module {
    private double originalGamma;

    public Fullbright() {
        super("Fullbright", "See in the dark.", Category.UTILITIES);
    }

    @Override
    public void onEnable() {
        originalGamma = mc.options.getGamma().getValue();
        mc.options.getGamma().setValue(100.0);
    }

    @Override
    public void onDisable() {
        mc.options.getGamma().setValue(originalGamma);
    }
}