package com.github.xupie.guildbridge.commands;

import com.github.xupie.guildbridge.config.ModConfig;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommands.literal;

@Environment(EnvType.CLIENT)
public final class GuildBridgeCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal("guildbridge")
                .executes(GuildBridgeCommand::openConfig)
        );
    }

    private static int openConfig(CommandContext<FabricClientCommandSource> context) {
        Screen configScreen = ModConfig.configScreen(null);
        Minecraft.getInstance().gui.setScreen(configScreen);
        return Command.SINGLE_SUCCESS;
    }
}