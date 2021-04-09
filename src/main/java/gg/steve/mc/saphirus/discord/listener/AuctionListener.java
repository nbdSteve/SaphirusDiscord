package gg.steve.mc.saphirus.discord.listener;

import fr.maxlego08.zauctionhouse.api.AuctionItem;
import fr.maxlego08.zauctionhouse.api.event.events.AuctionSellEvent;
import gg.steve.mc.saphirus.discord.SaphirusDiscord;
import gg.steve.mc.saphirus.discord.bot.DiscordBotUtil;
import gg.steve.mc.saphirus.discord.framework.utils.LogUtil;
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
import java.util.TreeMap;

public class AuctionListener implements Listener {

    @EventHandler
    public void onSale(AuctionSellEvent event) {
        if (DiscordBotUtil.getJda() == null) {
            LogUtil.info("cannot find jda");
        }
        TextChannel channel;
        try {
            channel = DiscordBotUtil.getJda().getTextChannelById(Files.CONFIG.get().getLong("discord.channel"));
        } catch (Exception e) {
            LogUtil.info("Cannot find channel");
            return;
        }
        if (channel == null) {
            LogUtil.info("Cannot find the channel");
        }
        Bukkit.getScheduler().runTaskAsynchronously(SaphirusDiscord.getInstance(), () -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(getColor());
            builder.setTitle(Files.CONFIG.get().getString("message.header"));
            builder.setFooter(Files.CONFIG.get().getString("message.footer"));
            if (Files.CONFIG.get().getBoolean("discord.time-stamp")) builder.setTimestamp(OffsetDateTime.now());
            String buyer = "";
            try {
                buyer = event.getAuctionItem().getBuyer().getName();
            } catch (IllegalArgumentException e) {
                buyer = "No Buyer.";
            }
            StringBuilder string = new StringBuilder();
            for (int i = 0; i < Files.CONFIG.get().getStringList("message.body").size(); i++) {
                String line = Files.CONFIG.get().getStringList("message.body").get(i);
                line = line.replace("{player}", event.getPlayer().getName())
                        .replace("{item-name}", getName(event.getAuctionItem().getItemStack()))
                        .replace("{buyer}", buyer)
                        .replace("{amount}", SaphirusDiscord.format(event.getAuctionItem().getAmount()))
                        .replace("{material}", event.getAuctionItem().getItemStack().getType().name())
                        .replace("{price}", SaphirusDiscord.format(event.getPrice()))
                        .replace("{currency}", event.getEconomy().toCurrency())
                        .replace("{lore}", getLore(event.getAuctionItem().getItemStack()))
                        .replace("{enchantments}", getEnchants(event.getAuctionItem().getItemStack()));
                string.append(line + "\n");
            }
//            for (int i = 0; i < Files.CONFIG.get().getStringList("message.body").size(); i++) {
//                String line = Files.CONFIG.get().getStringList("message.body").get(i);
//                line = line.replace("{player}", event.getPlayer().getName())
//                        .replace("{item-name}", getName(event.getAuctionItem().getItemStack()))
//                        .replace("{buyer}", buyer)
//                        .replace("{amount}", SaphirusDiscord.format(event.getAuctionItem().getAmount()))
//                        .replace("{material}", event.getAuctionItem().getItemStack().getType().name())
//                        .replace("{price}", SaphirusDiscord.format(event.getPrice()))
//                        .replace("{currency}", event.getEconomy().toCurrency())
//                        .replace("{lore}", getLore(event.getAuctionItem().getItemStack()))
//                        .replace("{enchantments}", getEnchants(event.getAuctionItem().getItemStack()));
//                builder.addField("", line, false);
//            }
            string.trimToSize();
            builder.addField("", string.toString(), false);
            channel.sendMessage(builder.build()).complete();
        });
    }

    public String getEnchants(ItemStack item) {
        StringBuilder builder = new StringBuilder();
        if (item.hasItemMeta() && item.getItemMeta().hasEnchants()) {
            for (Enchantment enchant : item.getItemMeta().getEnchants().keySet()) {
                builder.append(Files.CONFIG.get().getString("enchant-map." + enchant.getName().toLowerCase()) + " " + integerToRoman5(item.getEnchantmentLevel(enchant)) + "\n");
            }
//            builder.delete(builder.length() - 2, builder.length() - 1);
        } else {
            builder.append("No enchanments.");
        }
        builder.trimToSize();
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
//            builder.delete(builder.length() - 2, builder.length() - 1);
        } else {
            builder.append("No lore.");
        }
        builder.trimToSize();
        return builder.toString();
    }

    public Color getColor() {
        String[] parts = Files.CONFIG.get().getString("discord.embed-color").split(":");
        return new Color(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    private static final TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
    static {
        treemap.put(1000, "M");
        treemap.put(900, "CM");
        treemap.put(500, "D");
        treemap.put(400, "CD");
        treemap.put(100, "C");
        treemap.put(90, "XC");
        treemap.put(50, "L");
        treemap.put(40, "XL");
        treemap.put(10, "X");
        treemap.put(9, "IX");
        treemap.put(5, "V");
        treemap.put(4, "IV");
        treemap.put(1, "I");

    }

    public static final String integerToRoman5(int number) {
        int l = treemap.floorKey(number);
        if (number == l) {
            return treemap.get(number);
        }
        return treemap.get(l) + integerToRoman5(number - l);
    }
}
