package com.github.xupie.guildbridge.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModConfig {
    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
        .serializer(config -> GsonConfigSerializerBuilder.create(config)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("guildbridge.json"))
            .build())
        .build();

    @SerialEntry
    public String botIGN = "IsleofDucks";

    @SerialEntry
    public boolean compactGuildMessage = false;

    @SerialEntry
    public boolean addRank = true;

    @SerialEntry
    public String rank = "§d[MEOW++]";

    @SerialEntry
    public boolean addPrefix = true;

    @SerialEntry
    public boolean addDucklingPrefix = true;

    @SerialEntry
    public Guild guildOption = Guild.DUCK;

    @SerialEntry
    public String prefix = "§e[BRIDGE]";

    @SerialEntry
    public Color playerNameColor = Color.CYAN;

    @SerialEntry
    public boolean autoWelcome = true;

    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) -> {
            final Component title = Component.literal("Guild Bridge Config");
            builder.title(title);

            final ConfigCategory.Builder category = ConfigCategory.createBuilder();
            category.name(Component.literal("General"));
            // bot IGN
            category.option(option(
                "Bot IGN",
                "",
                "IsleofDucks",
                () -> config.botIGN,
                value -> config.botIGN = value,
                StringControllerBuilder::create
            ));
            // Compact Guild Message
            category.option(option(
                "Compact Guild Message",
                "Changes \"Guild >\" to \"G>\"",
                false,
                () -> config.compactGuildMessage,
                value -> config.compactGuildMessage = value,
                opt -> BooleanControllerBuilder.create(opt).coloured(true)
            ));
            // Add Rank
            category.option(option(
                "Add Rank",
                "Adds Rank to bridge's messages",
                true,
                () -> config.addRank,
                value -> config.addRank = value,
                opt -> BooleanControllerBuilder.create(opt).coloured(true)
            ));
            // Rank
            category.option(option(
                "Rank",
                "",
                "§d[MEOW]",
                () -> config.rank,
                value -> config.rank = value,
                StringControllerBuilder::create
            ));
            // Add Prefix
            category.option(option(
                "Add Prefix",
                "Adds Prefix to bridge's messages",
                true,
                () -> config.addPrefix,
                value -> config.addPrefix = value,
                opt -> BooleanControllerBuilder.create(opt).coloured(true)
            ));
            // Use Duck or Duckling as prefix
            category.option(option(
                "Use Duck or Duckling as prefix for other guild",
                "This doesn't detect if message is sent from the Discord bridge",
                true,
                () -> config.addDucklingPrefix,
                value -> config.addDucklingPrefix = value,
                opt -> BooleanControllerBuilder.create(opt).coloured(true)
            ));
            // Guild
            category.option(option(
                "Guild",
                "",
                Guild.DUCK,
                () -> config.guildOption,
                value -> config.guildOption = value,
                opt -> EnumControllerBuilder.create(opt).enumClass(Guild.class)
            ));
            // Prefix
            category.option(option(
                "Prefix",
                "",
                "§e[BRIDGE]",
                () -> config.prefix,
                value -> config.prefix = value,
                StringControllerBuilder::create
            ));
            // Player Name Color
            category.option(option(
                "Player Name Color",
                "",
                Color.CYAN,
                () -> config.playerNameColor,
                value -> config.playerNameColor = value,
                ColorControllerBuilder::create
            ));
            // Auto Welcome Ducksicle
            category.option(option(
                "Auto Welcome Ducksicle",
                "",
                false,
                () -> config.autoWelcome,
                value -> config.autoWelcome = value,
                opt -> BooleanControllerBuilder.create(opt).coloured(true)
            ));
            builder.category(category.build());

            return builder;
        }).generateScreen(parent);
    }

    private static <T> Option<T> option(
        String name,
        String description,
        T defaultValue,
        Supplier<T> getter,
        Consumer<T> setter,
        Function<Option<T>, ControllerBuilder<T>> controller) {

        return Option.<T>createBuilder()
            .name(Component.literal(name))
            .description(OptionDescription.of(Component.literal(description)))
            .binding(defaultValue, getter, setter)
            .controller(controller)
            .build();
    }

    public enum Guild implements NameableEnum {
        DUCK,
        DUCKLING;

        @Override
        public Component getDisplayName() {
            return Component.literal(name());
        }
    }
}
