package nl.xudes.client.gui.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import nl.xudes.client.module.Category;

import java.awt.Color;

public class CategoryButton extends ButtonWidget {
    private final Category category;
    private boolean active;

    public CategoryButton(int x, int y, int width, int height, Category category, PressAction onPress) {
        super(x, y, width, height, Text.literal(category.getName()), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.category = category;
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        int color = active ? new Color(64, 64, 64, 200).getRGB() : new Color(30, 30, 30, 200).getRGB();
        
        if (this.isSelected()) {
            color = new Color(80, 80, 80, 200).getRGB();
        }

        context.fill(getX(), getY(), getX() + width, getY() + height, color);

        int textColor = active ? 0xFF5555FF : 0xFFFFFFFF;
        context.drawCenteredTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            this.getMessage(),
            getX() + width / 2,
            getY() + (height - 8) / 2,
            textColor
        );
    }
    
    public Category getCategory() {
        return category;
    }
}