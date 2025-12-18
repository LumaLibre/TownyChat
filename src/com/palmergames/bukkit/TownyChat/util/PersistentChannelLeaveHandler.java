package com.palmergames.bukkit.TownyChat.util;

import com.google.gson.Gson;
import com.palmergames.bukkit.TownyChat.Chat;
import io.leangen.geantyref.TypeToken;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PersistentChannelLeaveHandler {

    public static final NamespacedKey IGNORED_CHANNELS_KEY = new NamespacedKey(Chat.getTownyChat(), "ignored_channels");
    private static final Gson GSON = new Gson();
    private static final Type STRING_LIST_TYPE_TOKEN = new TypeToken<List<String>>(){}.getType();


    private final Player player;

    public PersistentChannelLeaveHandler(Player player) {
        this.player = player;
    }

    public List<String> getIgnoredChannels() {
        String rawData = player.getPersistentDataContainer().get(IGNORED_CHANNELS_KEY, PersistentDataType.STRING);
        if (rawData == null || rawData.isEmpty()) {
            return new ArrayList<>();
        }
        return GSON.fromJson(rawData, STRING_LIST_TYPE_TOKEN);
    }

    public boolean isIgnoringChannel(String channelName) {
        List<String> ignoredChannels = getIgnoredChannels();
        return ignoredChannels.contains(channelName);
    }

    public void addIgnoredChannel(String channelName) {
        List<String> ignoredChannels = getIgnoredChannels();
        if (!ignoredChannels.contains(channelName)) {
            ignoredChannels.add(channelName);
            String json = GSON.toJson(ignoredChannels, STRING_LIST_TYPE_TOKEN);
            player.getPersistentDataContainer().set(IGNORED_CHANNELS_KEY, PersistentDataType.STRING, json);
        }
    }

    public void removeIgnoredChannel(String channelName) {
        List<String> ignoredChannels = getIgnoredChannels();
        if (!ignoredChannels.remove(channelName)) {
            return;
        }

        String json = GSON.toJson(ignoredChannels, STRING_LIST_TYPE_TOKEN);
        player.getPersistentDataContainer().set(IGNORED_CHANNELS_KEY, PersistentDataType.STRING, json);
        if (ignoredChannels.isEmpty()) {
            player.getPersistentDataContainer().remove(IGNORED_CHANNELS_KEY);
        }
    }
}
