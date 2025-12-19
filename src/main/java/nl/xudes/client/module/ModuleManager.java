package nl.xudes.client.module;

import nl.xudes.client.module.combat.AutoClicker;
import nl.xudes.client.module.util.Sprint;
import nl.xudes.client.module.util.Fullbright;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {
    private static final ModuleManager INSTANCE = new ModuleManager();
    private final List<Module> modules = new ArrayList<>();

    private ModuleManager() {
        // Register Modules Here
        modules.add(new AutoClicker());
        modules.add(new Sprint());
        modules.add(new Fullbright());
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
}