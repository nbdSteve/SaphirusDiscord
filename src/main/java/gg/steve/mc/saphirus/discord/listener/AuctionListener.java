package gg.steve.mc.saphirus.discord.listener;

import fr.maxlego08.zauctionhouse.api.AuctionItem;
import fr.maxlego08.zauctionhouse.api.event.events.AuctionSellEvent;
import gg.steve.mc.saphirus.discord.SaphirusDiscord;
import gg.steve.mc.saphirus.discord.bot.DiscordBotUtil;
import gg.steve.mc.saphirus.discord.framework.yml.Files;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.time.OffsetDateTime;

public class AuctionListener implements Listener {

    @EventHandler
    public void onSale(AuctionSellEvent event) {
        TextChannel channel = DiscordBotUtil.getJda().getTextChannelById(Files.CONFIG.get().getInt("channel"));
        Bukkit.getScheduler().runTaskAsynchronously(SaphirusDiscord.getInstance(), () -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(getColor());
            builder.setTitle(Files.CONFIG.get().getString("message.header"));
            builder.setFooter(Files.CONFIG.get().getString("message.footer"));
            if (Files.CONFIG.get().getBoolean("discord.time-stamp")) builder.setTimestamp(OffsetDateTime.now());
            for (int i = 0; i < Files.CONFIG.get().getStringList("message.body").size(); i++) {
                String line = Files.CONFIG.get().getStringList("message.body").get(i);
                line = line.replace("{player}", event.getPlayer().getName())
                        .replace("{buyer}", event.getAuctionItem().getBuyer().getName())
                        .replace("{amount}", SaphirusDiscord.format(event.getAuctionItem().getAmount()))
                        .replace("{material}", event.getAuctionItem().getItemStack().getType().name())
                        .replace("{price}", SaphirusDiscord.format(event.getPrice()))
                        .replace("{currency}", event.getEconomy().toCurrency())
                        .replace("{lore}", getLore(event.getAuctionItem().getItemStack()))
                        .replace("{enchantments}", getEnchants(event.getAuctionItem().getItemStack()));
                builder.addField(String.valueOf(i), line, false);
            }
            channel.sendMessage(builder.build()).complete();
        });
    }

    public String getEnchants(ItemStack item) {
        StringBuilder builder = new StringBuilder();
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            for (Enchantment enchant : item.getItemMeta().getEnchants().keySet()) {
                builder.append(enchant.getName() + " " + item.getItemMeta().getEnchantLevel(enchant) + "\n");
            }
            builder.delete(builder.length() - 3, builder.length() - 1);
        } else {
            builder.append("No enchanments.");
        }
        return builder.toString();
    }

    public String getName(ItemStack item) {
        StringBuilder builder = new StringBuilder();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            builder.append(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
        } else {
            builder.append("No name.");
        }
        return builder.toString();
    }

    public String getLore(ItemStack item) {
        StringBuilder builder = new StringBuilder();
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String line : item.getItemMeta().getLore()) {
                builder.append(ChatColor.stripColor(line) + "\n");
            }
            builder.delete(builder.length() - 3, builder.length() - 1);
        } else {
            builder.append("No lore.");
        }
        return builder.toString();
    }

    public Color getColor() {
        String[] parts = Files.CONFIG.get().getString("discord.color").split(":");
        return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}
