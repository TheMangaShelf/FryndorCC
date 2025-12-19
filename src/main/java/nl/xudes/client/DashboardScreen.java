package nl.xudes.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.ArrayList;
import java.util.List;

public class DashboardScreen extends Screen {

    private static final List<Module> modules = new ArrayList<>();

    static {
        modules.add(new Module("Sprint"));
        modules.add(new Module("Fullbright"));
        modules.add(new Module("FPS Display"));
        modules.add(new Module("Keystrokes"));
        modules.add(new Module("TNT Timer"));
        modules.add(new Module("Zoom"));
    }

    public DashboardScreen() {
        super(Text.literal("FryndorCC"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        int btnWidth = 100;
        int btnHeight = 25;
        int padding = 10;

        for (int i = 0; i < modules.size(); i++) {
            Module mod = modules.get(i);
            int col = i % 2;
            int row = i / 2;
            
            int xPos = centerX - 105 + (col * (btnWidth + padding));
            int yPos = centerY - 60 + (row * (btnHeight + padding));
            this.addDrawableChild(new ModernButton(xPos, yPos, btnWidth, btnHeight, mod, button -> {
                mod.toggle();
            }));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xCC000000);
        context.getMatrices().push();
        context.getMatrices().scale(2.0f, 2.0f, 2.0f);
        context.drawCenteredTextWithShadow(
            this.textRenderer, 
            this.title, 
            this.width / 2 / 2,
            (this.height / 2 - 100) / 2, 
            0xFF5555FF
        );
        
        context.getMatrices().pop();

        super.render(context, mouseX, mouseY, delta);
    }
}