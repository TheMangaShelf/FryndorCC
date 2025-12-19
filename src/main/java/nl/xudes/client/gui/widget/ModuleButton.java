package nl.xudes.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import nl.xudes.client.module.Module;

import java.awt.Color;

public class ModuleButton extends ButtonWidget {
    private final Module module;

    public ModuleButton(int x, int y, int width, int height, Module module) {
        super(x, y, width, height, Text.literal(module.getName()), button -> module.toggle(), DEFAULT_NARRATION_SUPPLIER);
        this.module = module;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int bgColor = module.isEnabled() ? new Color(46, 204, 113).getRGB() : new Color(45, 52, 54).getRGB();
        
        if (this.isSelected()) {
             bgColor = module.isEnabled() ? new Color(39, 174, 96).getRGB() : new Color(99, 110, 114).getRGB();
        }

        context.fill(getX(), getY(), getX() + width, getY() + height, bgColor);

        context.drawBorder(getX(), getY(), width, height, 0x50FFFFFF);

        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            this.getMessage(),
            getX() + width / 2,
            getY() + (height - 8) / 2,
            0xFFFFFFFF
        );
    }
}