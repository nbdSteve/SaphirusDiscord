package gg.steve.mc.saphirus.discord.bot;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class MessageEmbedField {
    private List<EmbedPlaceholderType> placeholders;
    private String tile, line;

    public MessageEmbedField(String tile, String line) {
        this.tile = tile;
        this.line = line;
        this.placeholders = EmbedPlaceholderType.getPlaceholders(line);
    }
}
