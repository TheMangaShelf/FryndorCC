package nl.xudes.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;


public class ModernButton extends ButtonWidget {

    private Module module;

    public ModernButton(int x, int y, int width, int height, Module module, PressAction onPress) {
        super(x, y, width, height, Text.literal(module.getName()), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.module = module;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int backgroundColor = module.isEnabled() ? 0xFF2ecc71 : 0xFF2d3436;
        int textColor = 0xFFffffff;

        if (this.isSelected()) {
            backgroundColor = module.isEnabled() ? 0xFF27ae60 : 0xFF636e72;
        }

        context.fill(getX(), getY(), getX() + width, getY() + height, backgroundColor);
        context.drawBorder(getX(), getY(), width, height, 0x50FFFFFF);
        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer, 
            this.getMessage(), 
            getX() + width / 2, 
            getY() + (height - 8) / 2, 
            textColor
        );
    }
}