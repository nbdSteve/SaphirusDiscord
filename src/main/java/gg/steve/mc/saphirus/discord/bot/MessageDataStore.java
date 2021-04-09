package gg.steve.mc.saphirus.discord.bot;

import fr.maxlego08.zauctionhouse.api.AuctionItem;
import gg.steve.mc.saphirus.discord.framework.utils.LogUtil;
import gg.steve.mc.saphirus.discord.framework.yml.Files;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.configuration.file.YamlConfiguration;

public class MessageDataStore {

    public static void saveMessage(AuctionItem item, long messageId) {
        YamlConfiguration data = Files.DATA.get();
        data.set(String.valueOf(item.getUniqueId()), messageId);
        Files.DATA.save();
    }

    public static void removeMessage(AuctionItem item) {
        long messageId;
        try {
            messageId = Files.DATA.get().getLong(String.valueOf(item.getUniqueId()));
        } catch (Exception e) {
            LogUtil.warning("Unable to find message id in the respective channel, aborting.");
            return;
        }
        TextChannel channel = DiscordBotUtil.getChannel();
        Message message = channel.retrieveMessageById(messageId).complete();
        if (message == null) {
            LogUtil.warning("Unable to find message");
            return;
        }
        message.delete().queue();
        YamlConfiguration data = Files.DATA.get();
        data.set(String.valueOf(item.getUniqueId()), null);
        Files.DATA.save();
    }
}