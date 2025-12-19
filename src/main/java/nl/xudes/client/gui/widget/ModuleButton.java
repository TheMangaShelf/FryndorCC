package nl.xudes.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import nl.xudes.client.module.Module;
import nl.xudes.client.setting.BooleanSetting;
import nl.xudes.client.setting.NumberSetting;
import nl.xudes.client.setting.Setting;

import java.awt.Color;

public class ModuleButton extends ButtonWidget {
    private final Module module;
    private boolean expanded = false;
    private final int settingHeight = 15;
    
    // New: Track which setting is being dragged to fix "Can't move slider"
    private NumberSetting draggingSetting = null;

    public ModuleButton(int x, int y, int width, int height, Module module) {
        super(x, y, width, height, Text.literal(module.getName()), button -> module.toggle(), DEFAULT_NARRATION_SUPPLIER);
        this.module = module;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 1. Handle Main Button Clicks
        if (isHovered(mouseX, mouseY, getY(), height)) {
            if (button == 0) { // Left Click -> Toggle
                module.toggle();
                playDownSound(MinecraftClient.getInstance().getSoundManager());
                return true;
            } else if (button == 1) { // Right Click -> Expand
                this.expanded = !this.expanded;
                playDownSound(MinecraftClient.getInstance().getSoundManager());
                return true;
            }
        }

        // 2. Handle Setting Clicks
        if (expanded) {
            int currentY = getY() + height;
            for (Setting setting : module.getSettings()) {
                if (isHovered(mouseX, mouseY, currentY, settingHeight)) {
                    if (setting instanceof BooleanSetting boolSet) {
                        if (button == 0) {
                            boolSet.toggle();
                            playDownSound(MinecraftClient.getInstance().getSoundManager());
                            return true;
                        }
                    }
                    if (setting instanceof NumberSetting numSet) {
                        if (button == 0) {
                            // Fix: Use getX() instead of getY(), and lock the drag
                            this.draggingSetting = numSet;
                            handleSlider(numSet, mouseX, getX(), width);
                            return true;
                        }
                    }
                }
                currentY += settingHeight;
            }
        }

        return false;
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Release the slider lock
        this.draggingSetting = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // If we are locked onto a slider, update it regardless of mouse position
        if (this.draggingSetting != null) {
            handleSlider(this.draggingSetting, mouseX, getX(), width);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }
    
    private void handleSlider(NumberSetting numSet, double mouseX, int x, int width) {
        double diff = Math.min(width, Math.max(0, mouseX - x));
        double percentage = diff / width;
        double range = numSet.getMax() - numSet.getMin();
        double val = numSet.getMin() + (range * percentage);
        numSet.setValue(val);
    }

    private boolean isHovered(double mouseX, double mouseY, int y, int height) {
        return mouseX >= getX() && mouseX <= getX() + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Main Button
        int mainColor = module.isEnabled() ? new Color(46, 204, 113).getRGB() : new Color(45, 52, 54).getRGB();
        context.fill(getX(), getY(), getX() + width, getY() + height, mainColor);
        context.drawBorder(getX(), getY(), width, height, 0x50FFFFFF);
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), getX() + width / 2, getY() + (height - 8) / 2, 0xFFFFFFFF);

        // Settings
        if (expanded) {
            int currentY = getY() + height;
            for (Setting setting : module.getSettings()) {
                // Background
                context.fill(getX(), currentY, getX() + width, currentY + settingHeight, new Color(30, 30, 30).getRGB());
                context.drawBorder(getX(), currentY, width, settingHeight, 0x20FFFFFF);

                if (setting instanceof BooleanSetting boolSet) {
                    String text = boolSet.getName() + ": " + (boolSet.isEnabled() ? "ON" : "OFF");
                    int color = boolSet.isEnabled() ? 0xFF55FF55 : 0xFFFF5555;
                    context.drawText(MinecraftClient.getInstance().textRenderer, text, getX() + 5, currentY + 4, color, true);
                } 
                else if (setting instanceof NumberSetting numSet) {
                    // Slider Bar
                    double percentage = (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin());
                    int sliderWidth = (int) (width * percentage);
                    
                    context.fill(getX(), currentY + settingHeight - 2, getX() + sliderWidth, currentY + settingHeight, 0xFFd67fff); // Purple-ish from python script
                    
                    String text = numSet.getName() + ": " + String.format("%.1f", numSet.getValue());
                    context.drawText(MinecraftClient.getInstance().textRenderer, text, getX() + 5, currentY + 3, 0xFFFFFFFF, true);
                }

                currentY += settingHeight;
            }
        }
    }
    
    public int getTotalHeight() {
        if (!expanded) return height;
        return height + (module.getSettings().size() * settingHeight);
    }
}