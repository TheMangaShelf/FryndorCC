package nl.xudes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import nl.xudes.client.gui.DashboardScreen;
import nl.xudes.client.module.ModuleManager;
import org.lwjgl.glfw.GLFW;

public class FryndorCC implements ClientModInitializer {

    private static KeyBinding homeKeyBinding;

    @Override
    public void onInitializeClient() {
        ModuleManager.getInstance();
        homeKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fryndorcc.menu",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.fryndorcc.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (homeKeyBinding.wasPressed()) {
                client.setScreen(new DashboardScreen());
            }

            if (client.player != null && client.world != null) {
                ModuleManager.getInstance().onTick();
            }
        });

        HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
            float tickDelta = tickDeltaManager.getTickDelta(false);
            ModuleManager.getInstance().onRender(context, tickDelta);
        });
    }
}