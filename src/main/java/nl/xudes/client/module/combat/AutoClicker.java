package nl.xudes.client.module.combat;

import nl.xudes.client.module.Category;
import nl.xudes.client.module.Module;
import net.minecraft.util.Hand;

public class AutoClicker extends Module {
    private int cooldown = 0;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks when holding left mouse.", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.currentScreen == null && mc.options.attackKey.isPressed()) {
            if (cooldown > 0) {
                cooldown--;
            } else {
                if (mc.interactionManager != null && mc.player != null) {
                   mc.player.swingHand(Hand.MAIN_HAND);

                   mc.interactionManager.attackEntity(mc.player, mc.crosshairTarget);
                }
                cooldown = 2; 
            }
        } else {
            cooldown = 0;
        }
    }
}