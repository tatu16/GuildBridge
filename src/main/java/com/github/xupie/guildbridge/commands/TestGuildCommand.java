package com.github.xupie.guildbridge.commands;

import com.github.xupie.guildbridge.features.GuildBridge;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

@Environment(EnvType.CLIENT)
public final class TestGuildCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal("testguild")
                .executes(TestGuildCommand::broadcast)
        );
    }

    private static int broadcast(CommandContext<FabricClientCommandSource> context) {
        Component processed = GuildBridge.register(
            Component.literal("§2Guild > §a[VIP§6+§a] Xupie §e[EMBER]§f: b"),
            true
        );

        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.gui.hud.getChat().addClientSystemMessage(processed);
        }

        return Command.SINGLE_SUCCESS;
    }
}