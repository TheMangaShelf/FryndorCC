package nl.xudes.client.module.util;

import net.minecraft.client.gui.DrawContext;
import nl.xudes.client.module.Category;
import nl.xudes.client.module.Module;
import org.lwjgl.glfw.GLFW;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class CPSDisplay extends Module {
    
    private final List<Long> clicks = new ArrayList<>();
    private boolean wasPressed = false;

    public CPSDisplay() {
        super("CPS Display", "Shows your clicks per second.", Category.UTILITIES);
    }

    @Override
    public void onTick() {
        long windowHandle = mc.getWindow().getHandle();
        int action = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_1);

        if (action == GLFW.GLFW_PRESS) {
            if (!wasPressed) {
                clicks.add(System.currentTimeMillis());
                wasPressed = true;
            }
        } else {
            wasPressed = false;
        }
        long currentTime = System.currentTimeMillis();
        clicks.removeIf(time -> time < currentTime - 1000);
    }

    @Override
    public void onRender(DrawContext context, float tickDelta) {
        String text = "CPS: " + clicks.size();
        context.drawText(mc.textRenderer, text, 10, 10, Color.WHITE.getRGB(), true);
    }
}