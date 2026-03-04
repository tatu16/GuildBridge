package com.github.xupie.guildbridge.features;

import com.github.xupie.guildbridge.config.ModConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class AutoWelcome {

    private static long lastWelcome = 0;

    public static Text register(Text textMessage, boolean b) {
        if (!ModConfig.HANDLER.instance().autoWelcome) return textMessage;

        if (System.currentTimeMillis() - lastWelcome >= 300_000) {
            if (Formatting.strip(textMessage.getString()).equals("Guild > Ducksicle joined.")) {
                MinecraftClient client = MinecraftClient.getInstance();

                if (client.player != null && client.player.networkHandler != null) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            client.player.networkHandler.sendChatCommand("gchat Welcome Princess!");
                            lastWelcome = System.currentTimeMillis();
                        } catch (InterruptedException ignored) {}
                    }).start();
                }
            }
        }

        return textMessage;
    }
}
