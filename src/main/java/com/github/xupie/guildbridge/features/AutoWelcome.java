package com.github.xupie.guildbridge.features;

import com.github.xupie.guildbridge.config.ModConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class AutoWelcome {

    private static long lastWelcome = 0;

    public static Component register(Component textMessage, boolean b) {
        if (!ModConfig.HANDLER.instance().autoWelcome) return textMessage;

        if (System.currentTimeMillis() - lastWelcome >= 300_000) {
            if (ChatFormatting.stripFormatting(textMessage.getString()).equals("Guild > Ducksicle joined.")) {
                Minecraft client = Minecraft.getInstance();

                if (client.player != null) {
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000);
                            client.player.connection.sendCommand("gchat Welcome Princess!");
                            lastWelcome = System.currentTimeMillis();
                        } catch (InterruptedException ignored) {}
                    }).start();
                }
            }
        }

        return textMessage;
    }
}
