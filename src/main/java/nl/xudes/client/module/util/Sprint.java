package nl.xudes.client.module.util;

import nl.xudes.client.module.Category;
import nl.xudes.client.module.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically toggles sprint.", Category.UTILITIES);
    }

    @Override
    public void onTick() {
        if (mc.player != null && !mc.player.isSneaking() && (mc.player.forwardSpeed > 0 || mc.player.sidewaysSpeed != 0)) {
            mc.player.setSprinting(true);
        }
    }
}