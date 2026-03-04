package com.github.xupie.guildbridge.features;

import com.github.xupie.guildbridge.config.ModConfig;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildBridge {

    //§2Guild > §b[MVP§c+§b] IsleofDucks §e[STAFF]§f: Xupie: b
    //§2Guild > §a[VIP§6+§a] Xupie §e[EMBER]§f: b
    //§2Guild > §aXupie §ejoined.
    //§2Guild > §aXupie §eleft.
    //§2Guild > §a[VIP§6+§a] MangoMilkshake §e[STAFF]§f: ✧EmanCarrier ᴹᴼᴰ: Test
    //§2Guild > §7DuckiePrincess §e[STAFF]§f: ✧EmanCarrier ᴹᴼᴰ: Test
    //§2Guild > §b[MVP§c+§b] IsleofDucks §e[STAFF]§f: ˚Nonth: genius
    public static final Pattern GUILD_BRIDGE_MESSAGE_PATTERN = Pattern.compile("^§2Guild > §.(?:\\[.*] )?(?<bot>\\w+) §.\\[\\w+]§f: [✧˚]?(?<player>\\w+)(?: \\S*)?: (?<message>.*)$");
    public static final Pattern GUILD_MESSAGE_PATTERN = Pattern.compile("^§2Guild > .*?(?: |§f)(?<player>\\w+) §.\\[\\w+]§f: (?!✧)(?<message>.*)$");
    public static final Pattern GUILD_JOIN_LEFT = Pattern.compile("^§2Guild > §.(?<player>\\w+) §.(?<action>joined|left)\\.$");

    private static final String GUILD_PREFIX = "§2Guild >";
    private static final String GUILD_PREFIX_COMPACT = "§2G>";
    private static final String BRIDGE_PREFIX_COMPACT = "§2B>";
    private static final String BRIDGE_PREFIX_FULL = "§2Bridge >";

    public static Text register(Text textMessage, boolean b) {
        String msg = textMessage.getString();
        if (!msg.startsWith("§2Guild > ")) return textMessage;

        ModConfig config = ModConfig.HANDLER.instance();

        Matcher bridgeMatcher = GUILD_BRIDGE_MESSAGE_PATTERN.matcher(msg);
        if (bridgeMatcher.matches() && bridgeMatcher.group("bot").equals(config.botIGN.trim())) {
            return handleBridgeMessage(bridgeMatcher.group("player"), bridgeMatcher.group("message"), config);
        }

        Matcher guildMessage = GUILD_MESSAGE_PATTERN.matcher(msg);
        if (guildMessage.matches()) {
            return handleGuildMessage(guildMessage.group("player"), msg, config);
        }

        Matcher joinMessage = GUILD_JOIN_LEFT.matcher(msg);
        if (joinMessage.matches() && config.compactGuildMessage) {
            return Text.literal(msg.replace(GUILD_PREFIX, GUILD_PREFIX_COMPACT));
        }

        return textMessage;
    }

    private static Text handleBridgeMessage(String player, String message, ModConfig config) {
        String compact = config.compactGuildMessage ? BRIDGE_PREFIX_COMPACT : BRIDGE_PREFIX_FULL;
        String rank = config.addRank && config.rank != null ? " " + config.rank : "";
        String prefix = config.addPrefix && config.prefix != null ? " " + config.prefix : "";

        Color playerNameColor = config.playerNameColor;
        TextColor textColor = playerNameColor != null
            ? TextColor.fromRgb(playerNameColor.getRGB())
            : TextColor.fromFormatting(Formatting.AQUA);

        return Text.empty()
            .append(Text.literal(compact + rank + " "))
            .append(Text.literal(player + prefix).styled(style -> style
                .withColor(textColor)
                .withUnderline(false)
            ))
            .append("§f: " + message)
            .styled(style -> createPlayerStyle(player));
    }

    private static Text handleGuildMessage(String player, String message, ModConfig config) {
        if (config.compactGuildMessage) {
            message = message.replace(GUILD_PREFIX, GUILD_PREFIX_COMPACT);
        }

        return Text.literal(message)
            .styled(style -> createPlayerStyle(player));
    }

    private static Style createPlayerStyle(String player) {
        return Style.EMPTY
            .withClickEvent(new ClickEvent.RunCommand("/pv " + player))
            .withHoverEvent(new HoverEvent.ShowText(Text.literal("§eClick here to run /pv " + player)))
            .withUnderline(true)
            .withColor(Formatting.AQUA);
    }
}
