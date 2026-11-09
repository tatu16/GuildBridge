package com.github.xupie.guildbridge.commands;

import com.github.xupie.guildbridge.features.AutoWelcome;
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
public final class TestWelcomeCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(
            literal("testautowelcome")
                .executes(TestWelcomeCommand::broadcast)
        );
    }

    private static int broadcast(CommandContext<FabricClientCommandSource> context) {
        Component processed = AutoWelcome.register(
            Component.literal("Guild > Ducksicle joined."),
            true
        );

        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.gui.hud.getChat().addClientSystemMessage(processed);
        }

        return Command.SINGLE_SUCCESS;
    }
}