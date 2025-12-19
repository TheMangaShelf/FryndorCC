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
        super.init();
        buildInterface();
    }

    private void buildInterface() {
        this.clearChildren();
        
        // 1. Add Sidebar
        int sidebarY = HEADER_HEIGHT + 20;
        for (Category category : Category.values()) {
            CategoryButton btn = new CategoryButton(
                10, sidebarY, SIDEBAR_WIDTH - 20, 20, category, 
                button -> selectCategory(category)
            );
            if (category == currentCategory) btn.setActive(true);
            this.addDrawableChild(btn);
            sidebarY += 25;
        }

        // 2. Add Modules (Vertical List Layout)
        List<Module> modules = ModuleManager.getInstance().getModulesByCategory(currentCategory);
        int startX = SIDEBAR_WIDTH + 20;
        int currentY = HEADER_HEIGHT + 20;
        int btnWidth = 160; // Wider buttons for settings
        int btnHeight = 25;
        int padding = 5;

        for (Module mod : modules) {
            // We create the button. 
            // Note: In a real dynamic GUI, we would recalculate positions in render() 
            // or store the buttons in a list to offset them dynamically. 
            // For simplicity here, we add them, but the position update logic needs to happen 
            // because expansion changes height.
            
            ModuleButton modBtn = new ModuleButton(startX, currentY, btnWidth, btnHeight, mod);
            this.addDrawableChild(modBtn);
            
            // Initial spacing. 
            // WARNING: This static init means if you expand one, the others WON'T move immediately 
            // until we rebuild the interface. 
            // To fix this, we need to update positions in render().
            currentY += btnHeight + padding; 
        }
    }

    private void selectCategory(Category category) {
        this.currentCategory = category;
        buildInterface();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // We override mouseClicked to rebuild the interface if a Right Click (Expand) happens
        // This forces the layout to recalculate positions based on the new expanded heights.
        boolean result = super.mouseClicked(mouseX, mouseY, button);
        if (button == 1) { // Right click
            updateLayout(); 
        }
        return result;
    }
    
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
         // Forward drag events (for sliders) to children
         return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void updateLayout() {
        // Recalculate Y positions based on expansion states
        int currentY = HEADER_HEIGHT + 20;
        int padding = 5;
        
        for (var child : this.children()) {
            if (child instanceof ModuleButton modBtn) {
                modBtn.setY(currentY);
                currentY += modBtn.getTotalHeight() + padding;
            }
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