package com.github.xupie.guildbridge.commands;

import com.github.xupie.guildbridge.config.ModConfig;
import com.github.xupie.guildbridge.features.GuildBridge;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

@Environment(EnvType.CLIENT)
public final class TestBridgeCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal("testbridge")
                .executes(TestBridgeCommand::broadcast)
                .then(ClientCommands.literal("duckling")
                    .executes(TestBridgeCommand::broadcastDuckling))
        );
    }

    private static int broadcast(CommandContext<FabricClientCommandSource> context) {
        String botIGN = ModConfig.HANDLER.instance().botIGN;

        Component processed = GuildBridge.register(
            Component.literal("§2Guild > §b[MVP§c+§b] " + botIGN + " §e[STAFF]§f: Xupie: b"),
            true
        );

        send(processed);
        return Command.SINGLE_SUCCESS;
    }

    private static int broadcastDuckling(CommandContext<FabricClientCommandSource> context) {
        String botIGN = ModConfig.HANDLER.instance().botIGN;

        Component processed = GuildBridge.register(
            Component.literal("§2Guild > §b[MVP§c+§b] " + botIGN + " §e[STAFF]§f: ˚Xupie: b"),
            true
        );

        send(processed);
        return Command.SINGLE_SUCCESS;
    }

    private static void send(Component text) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.gui.hud.getChat().addClientSystemMessage(text);
        }
    }

}


