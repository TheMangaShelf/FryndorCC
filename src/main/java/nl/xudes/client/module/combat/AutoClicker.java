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

    // Logic state
    private long nextEventTime = 0;
    private boolean isHolding = false; // Tracks if we are currently "pressing" the mouse

    public AutoClicker() {
        super("AutoClicker", "Legit-style clicking.", Category.COMBAT);
        
        // Defaults matching the Python script (4 - 7 CPS)
        this.minCps = new NumberSetting("Min CPS", 4, 1, 20, 0.5);
        this.maxCps = new NumberSetting("Max CPS", 7, 1, 20, 0.5);
        
        this.addSetting(minCps);
        this.addSetting(maxCps);
    }

    @Override
    public void onTick() {
        // Only run if GUI is not open and Attack Key is held
        if (mc.currentScreen == null && mc.options.attackKey.isPressed()) {
            
            long currentTime = System.currentTimeMillis();

            if (currentTime >= nextEventTime) {
                if (!isHolding) {
                    // --- PRESS EVENT ---
                    performClick();
                    isHolding = true;
                    
                    // Python: time.sleep(random.uniform(0.04, 0.08))
                    // Hold the button for 40ms to 80ms
                    long holdTime = (long) (random(0.04, 0.08) * 1000);
                    nextEventTime = currentTime + holdTime;
                    
                } else {
                    // --- RELEASE EVENT ---
                    isHolding = false;
                    
                    // Reset attack cooldown visually (optional, helps legit feel)
                    if (mc.player != null) mc.player.resetLastAttackedTicks();

                    // Python: cps = random.uniform(min, max)
                    // Python: time.sleep(max(0, (1.0/cps) - 0.06))
                    double cps = random(minCps.getValue(), maxCps.getValue());
                    double delaySeconds = (1.0 / cps) - 0.06;
                    
                    // Safety: Ensure delay isn't negative
                    long delayTime = (long) (Math.max(0, delaySeconds) * 1000);
                    
                    nextEventTime = currentTime + delayTime;
                }
            }
        } else {
            // Reset state when not clicking
            isHolding = false;
            nextEventTime = 0;
        }
    }

    private void performClick() {
        if (mc.interactionManager != null && mc.player != null) {
            mc.player.swingHand(Hand.MAIN_HAND);

            // Only attack if looking at an entity
            if (mc.crosshairTarget instanceof EntityHitResult hitResult) {
                mc.interactionManager.attackEntity(mc.player, hitResult.getEntity());
            }
        }
    }

    private double random(double min, double max) {
        if (min >= max) return min;
        return ThreadLocalRandom.current().nextDouble(min, max);
    }
}