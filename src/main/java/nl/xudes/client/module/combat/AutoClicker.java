package nl.xudes.client.module.combat;

import nl.xudes.client.module.Category;
import nl.xudes.client.module.Module;
import nl.xudes.client.setting.NumberSetting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;

import java.util.concurrent.ThreadLocalRandom;

public class AutoClicker extends Module {

    private final NumberSetting minCps;
    private final NumberSetting maxCps;

    private long nextEventTime = 0;
    private boolean isHolding = false;

    public AutoClicker() {
        super("AutoClicker", "Legit clicking logic.", Category.COMBAT);

        this.minCps = new NumberSetting("Min CPS", 4, 1, 20, 0.5);
        this.maxCps = new NumberSetting("Max CPS", 8, 1, 20, 0.5);
        
        this.addSetting(minCps);
        this.addSetting(maxCps);
    }

    @Override
    public void onTick() {
        
        if (mc.currentScreen == null && mc.options.attackKey.isPressed()) {
            long currentTime = System.currentTimeMillis();

            if (currentTime >= nextEventTime) {
                if (!isHolding) {
                    performClick();
                    isHolding = true;
                    long holdTime = (long) (random(0.04, 0.08) * 1000);
                    nextEventTime = currentTime + holdTime;
                } else {
                    isHolding = false;
                    if (mc.player != null) {
                         mc.player.resetLastAttackedTicks();
                    }

                    double cps = random(minCps.getValue(), maxCps.getValue());
                    double delaySeconds = (1.0 / cps) - 0.06;
                    long delayTime = (long) (Math.max(0, delaySeconds) * 1000);
                    
                    nextEventTime = currentTime + delayTime;
                }
            }
            
        } else {
            isHolding = false;
            nextEventTime = 0;
        }
    }

    private void performClick() {
        if (mc.interactionManager != null && mc.player != null) {
            mc.player.swingHand(Hand.MAIN_HAND);

            if (mc.crosshairTarget instanceof EntityHitResult hitResult) {
                mc.interactionManager.attackEntity(mc.player, hitResult.getEntity());
            } else {
                // Use this to mine blocks or swing at air
                // We call the vanilla left-click handling
                // This is slightly complex to invoke safely from a module without Mixins,
                // but strictly attacking entities is the main goal of an autoclicker.
            }
        }
    }

    private double random(double min, double max) {
        if (min >= max) return min;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
    
    @Override
    public void onDisable() {
        isHolding = false;
    }
}