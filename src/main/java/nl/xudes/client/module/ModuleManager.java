package nl.xudes.client.module;

import nl.xudes.client.module.combat.AutoClicker;
import nl.xudes.client.module.util.Sprint;
import nl.xudes.client.module.util.Fullbright;
import nl.xudes.client.module.util.CPSDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.gui.DrawContext;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
        // Register Modules Here
        modules.add(new AutoClicker());
        modules.add(new Sprint());
        modules.add(new Fullbright());
        modules.add(new CPSDisplay());
    }

    public static ModuleManager getInstance() {
        return INSTANCE;
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        return modules.stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }

    public void onTick() {
        modules.stream().filter(Module::isEnabled).forEach(Module::onTick);
    }

    public void onRender(DrawContext context, float tickDelta) {
        modules.stream().filter(Module::isEnabled).forEach(m -> m.onRender(context, tickDelta));
    }
}