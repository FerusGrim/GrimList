/**
 * @author FerusGrim
 * @website http://ferusgrim.github.io/
 * Copyright under GPLv3 to Nicholas Badger (FerusGrim) - 2014
 */

package io.github.ferusgrim.GrimList.Commands;

import io.github.ferusgrim.GrimList.GrimList;
import io.github.ferusgrim.GrimList.utils.AsyncThenSyncOperation;
import io.github.ferusgrim.GrimList.utils.UUIDFetcher;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class ViewPlayer {
    private GrimList plugin;

    public ViewPlayer(GrimList plugin) {
        this.plugin = plugin;
    }

    public boolean run(CommandSender sender, String name) {
        switch (plugin.focusOn) {
            case "file":
                if (plugin.filem.isPlayersPopulated()) {
                    String uuid = plugin.filem.getUUID(name);
                    if (uuid.isEmpty()) {
                        runOperation(sender, name);
                    } else {
                        plugin.filem.ViewPlayer(sender, uuid);
                    }
                } else {
                    if (plugin.getConfig().getBoolean("SaveQueries")) {
                        runOperation(sender, name);
                    } else {
                        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record doesn't exist!");
                    }
                }
                break;
        }
        return true;
    }

    private void runOperation(CommandSender sender, String name) {
        sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Looking up UUID. This can take a moment...");
        new AsyncThenSyncOperation(plugin, true) {
            private Map<String, UUID> response = null;

            @Override
            protected void execAsyncFirst() {
                try {
                    response = new UUIDFetcher(Arrays.asList(name.toLowerCase())).call();
                } catch (Exception e) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Error while looking up UUID. Check Logs.");
                    plugin.log("SEVERE", "UUID ERROR : STACK TRACE");
                    e.printStackTrace();
                }
            }

            @Override
            protected void execSyncThen() {
                if (response.get(name.toLowerCase()) == null) {
                    sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "UUID Query returned null! Invalid username?");
                    return;
                }
                String uuid = response.get(name.toLowerCase()).toString();
                switch (plugin.focusOn) {
                    case "file":
                        if (plugin.filem.doesRecordExist(uuid)) {
                            plugin.filem.ViewPlayer(sender, uuid);
                        } else {
                            if (plugin.getConfig().getBoolean("SaveQueries")) {
                                plugin.filem.recordAfterIdLookup(uuid, name);
                                plugin.filem.ViewPlayer(sender, uuid);
                            } else {
                                sender.sendMessage((sender instanceof Player ? plugin.mStart : "") + "Player record doesn't exist!");
                            }
                        }
                        break;
                }
            }
        };
    }

}
