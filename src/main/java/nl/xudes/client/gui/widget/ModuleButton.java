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
    private final int headerHeight = 25; // Height of the main button part
    private final int settingHeight = 15;
    
    // Track which setting is being dragged
    private NumberSetting draggingSetting = null;

    public ModuleButton(int x, int y, int width, int height, Module module) {
        // Initialize with standard height
        super(x, y, width, height, Text.literal(module.getName()), button -> module.toggle(), DEFAULT_NARRATION_SUPPLIER);
        this.module = module;
        this.height = headerHeight; // Force initial height
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // 1. Handle Header Click (Toggle/Expand)
        if (mouseY >= getY() && mouseY < getY() + headerHeight && mouseX >= getX() && mouseX <= getX() + width) {
            if (button == 0) { // Left Click -> Toggle
                module.toggle();
                playDownSound(MinecraftClient.getInstance().getSoundManager());
                return true;
            } else if (button == 1) { // Right Click -> Expand
                this.expanded = !this.expanded;
                updateHeight(); // Update widget height for layout
                playDownSound(MinecraftClient.getInstance().getSoundManager());
                return true;
            }
        }

        // 2. Handle Settings
        if (expanded) {
            int currentY = getY() + headerHeight;
            for (Setting setting : module.getSettings()) {
                // Check if mouse is within this setting's area
                if (mouseY >= currentY && mouseY < currentY + settingHeight && mouseX >= getX() && mouseX <= getX() + width) {
                    
                    if (setting instanceof BooleanSetting boolSet && button == 0) {
                        boolSet.toggle();
                        playDownSound(MinecraftClient.getInstance().getSoundManager());
                        return true;
                    }
                    
                    if (setting instanceof NumberSetting numSet && button == 0) {
                        this.draggingSetting = numSet; // Lock drag
                        handleSlider(numSet, mouseX);
                        playDownSound(MinecraftClient.getInstance().getSoundManager());
                        return true;
                    }
                }
                currentY += settingHeight;
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.draggingSetting = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.draggingSetting != null) {
            handleSlider(this.draggingSetting, mouseX);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void handleSlider(NumberSetting numSet, double mouseX) {

        double diff = mouseX - getX();
        double percentage = Math.max(0, Math.min(1, diff / width));
        
        double range = numSet.getMax() - numSet.getMin();
        double val = numSet.getMin() + (range * percentage);
        numSet.setValue(val);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {

        int mainColor = module.isEnabled() ? new Color(46, 204, 113).getRGB() : new Color(45, 52, 54).getRGB();
        context.fill(getX(), getY(), getX() + width, getY() + headerHeight, mainColor);
        context.drawBorder(getX(), getY(), width, headerHeight, 0x50FFFFFF);
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), getX() + width / 2, getY() + (headerHeight - 8) / 2, 0xFFFFFFFF);

        if (expanded) {
            int currentY = getY() + headerHeight;
            for (Setting setting : module.getSettings()) {
                context.fill(getX(), currentY, getX() + width, currentY + settingHeight, new Color(30, 30, 30).getRGB());
                context.drawBorder(getX(), currentY, width, settingHeight, 0x20FFFFFF);

                if (setting instanceof BooleanSetting boolSet) {
                    String text = boolSet.getName() + ": " + (boolSet.isEnabled() ? "ON" : "OFF");
                    int color = boolSet.isEnabled() ? 0xFF55FF55 : 0xFFFF5555;
                    context.drawText(MinecraftClient.getInstance().textRenderer, text, getX() + 5, currentY + 4, color, true);
                } 
                else if (setting instanceof NumberSetting numSet) {
                    double percentage = (numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin());
                    int sliderWidth = (int) (width * percentage);
                    context.fill(getX(), currentY + settingHeight - 2, getX() + sliderWidth, currentY + settingHeight, 0xFFd67fff);
                    String text = numSet.getName() + ": " + String.format("%.1f", numSet.getValue());
                    context.drawText(MinecraftClient.getInstance().textRenderer, text, getX() + 5, currentY + 3, 0xFFFFFFFF, true);
                }
                currentY += settingHeight;
            }
        }
    }
    
    public void updateHeight() {
        if (!expanded) {
            this.height = headerHeight;
        } else {
            this.height = headerHeight + (module.getSettings().size() * settingHeight);
        }
    }

    public int getTotalHeight() {
        return this.height;
    }
}