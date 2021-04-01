package gg.steve.mc.saphirus.discord.bot;

import java.util.ArrayList;
import java.util.List;

public enum EmbedPlaceholderType {
    PLAYER("{player}"),
    AMOUNT("{amount}"),
    BUYER("{buyer}"),
    PRICE("{price}"),
    CURRENCY("{currency}"),
    MATERIAL("{material}"),
    ITEM_NAME("{item-name}"),
    LORE("{lore}"),
    ENCHANTMENTS("{enchantments}");

    private String placeholder;

    EmbedPlaceholderType(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public static List<EmbedPlaceholderType> getPlaceholders(String line) {
        List<EmbedPlaceholderType> placeholders = new ArrayList<>();
        for (EmbedPlaceholderType placeholderType : EmbedPlaceholderType.values()) {
            if (line.contains(placeholderType.getPlaceholder())) placeholders.add(placeholderType);
        }
        return placeholders;
    }
}
