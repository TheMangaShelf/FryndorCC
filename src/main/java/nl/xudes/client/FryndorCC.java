package nl.xudes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class FryndorCC implements ClientModInitializer {

    private static KeyBinding homeKeyBinding;

    @Override
    public void onInitializeClient() {
        homeKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.fryndorcc.k",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.fryndorcc.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (homeKeyBinding.wasPressed()) {
                client.setScreen(new DashboardScreen());
            }
        });
    }
}