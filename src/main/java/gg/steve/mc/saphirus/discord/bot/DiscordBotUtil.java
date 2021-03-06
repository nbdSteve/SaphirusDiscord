package gg.steve.mc.saphirus.discord.bot;

import fr.maxlego08.zauctionhouse.api.utils.Logger;
import gg.steve.mc.saphirus.discord.framework.utils.LogUtil;
import gg.steve.mc.saphirus.discord.framework.yml.Files;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.util.ArrayList;
import java.util.List;

public class DiscordBotUtil {
    private static JDA jda;

    public static void enableBot() {
        String token = Files.CONFIG.get().getString("discord.token");
        List<GatewayIntent> intents = new ArrayList<>();
        for (String intent : Files.CONFIG.get().getStringList("discord.gateway-intents")) {
            try {
                intents.add(GatewayIntent.valueOf(intent.toUpperCase()));
            } catch (Exception e) {
                LogUtil.warning("Error adding gateway intent " + intent.toUpperCase() + ", please verify that this is a valid gateway intent.");
            }
        }
        try {
            JDABuilder builder = JDABuilder.create(token, intents);
            builder.setMemberCachePolicy(MemberCachePolicy.ALL);
            jda = builder.build();
            jda.getPresence().setActivity(Activity.playing(Files.CONFIG.get().getString("discord.game")));
            Logger.info("Loading of the discord bot successfully completed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JDA getJda() {
        return jda;
    }

    public static TextChannel getChannel() {
        if (DiscordBotUtil.getJda() == null) {
            LogUtil.warning("cannot find jda");
        }
        TextChannel channel;
        try {
            channel = DiscordBotUtil.getJda().getTextChannelById(Files.CONFIG.get().getLong("discord.channel"));
        } catch (Exception e) {
            LogUtil.warning("Cannot find channel");
            return null;
        }
        if (channel == null) {
            LogUtil.warning("Cannot find the channel");
        }
        return channel;
    }
}
