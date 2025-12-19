package nl.xudes.client.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import nl.xudes.client.gui.widget.CategoryButton;
import nl.xudes.client.gui.widget.ModuleButton;
import nl.xudes.client.module.Category;
import nl.xudes.client.module.Module;
import nl.xudes.client.module.ModuleManager;

import java.awt.Color;
import java.util.List;

public class DashboardScreen extends Screen {
    
    private Category currentCategory = Category.COMBAT;

    private final int SIDEBAR_WIDTH = 100;
    private final int HEADER_HEIGHT = 40;

    public DashboardScreen() {
        super(Text.literal("FryndorCC Dashboard"));
    }

    @Override
    protected void init() {
        int sidebarY = HEADER_HEIGHT + 20;

        for (Category category : Category.values()) {
            CategoryButton btn = new CategoryButton(
                10, 
                sidebarY, 
                SIDEBAR_WIDTH - 20, 
                20, 
                category, 
                button -> selectCategory(category)
            );
            
            if (category == currentCategory) btn.setActive(true);
            
            this.addDrawableChild(btn);
            sidebarY += 25;
        }

        refreshModuleButtons();
    }

    private void selectCategory(Category category) {
        this.currentCategory = category;
        this.children().stream()
            .filter(element -> element instanceof CategoryButton)
            .map(element -> (CategoryButton) element)
            .forEach(btn -> btn.setActive(btn.getCategory() == category));

        refreshModuleButtons();
    }

    private void refreshModuleButtons() {
        this.clearChildren();
        this.init(); 
        List<Module> modules = ModuleManager.getInstance().getModulesByCategory(currentCategory);
        int startX = SIDEBAR_WIDTH + 20;
        int startY = HEADER_HEIGHT + 20;
        int btnWidth = 100;
        int btnHeight = 25;
        int padding = 10;

        for (int i = 0; i < modules.size(); i++) {
            Module mod = modules.get(i);
            int col = i % 3;
            int row = i / 3;

            int x = startX + (col * (btnWidth + padding));
            int y = startY + (row * (btnHeight + padding));

            this.addDrawableChild(new ModuleButton(x, y, btnWidth, btnHeight, mod));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.fill(0, 0, this.width, this.height, new Color(20, 20, 20, 150).getRGB());
        context.fill(0, 0, SIDEBAR_WIDTH, this.height, new Color(30, 30, 30, 255).getRGB());
        context.drawBorder(0, 0, SIDEBAR_WIDTH, this.height, 0x50FFFFFF);
        context.getMatrices().push();
        context.getMatrices().scale(1.5f, 1.5f, 1.5f);
        context.drawTextWithShadow(this.textRenderer, "FryndorCC", 10, 10, 0xFF5555FF);
        context.getMatrices().pop();

        super.render(context, mouseX, mouseY, delta);
    }
}