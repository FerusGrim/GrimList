package me.ferusgrim.grimlist.database;

import me.ferusgrim.grimlist.GrimList;
import me.ferusgrim.grimlist.storage.FileStorage;
import me.ferusgrim.grimlist.storage.HoconFileStorage;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HoconDatabase extends Database {

    public HoconDatabase(GrimList plugin) {
        super(plugin, DatabaseType.HOCON, new HoconFileStorage(plugin.getConfigDir(), plugin.getConfig().getHoconFilename()));
    }

    @Override
    public List<UUID> getWhitelistFromStorage() {
        List<UUID> whitelist = new ArrayList<UUID>();

        for (ConfigurationNode uuid : ((FileStorage) this.getStorage()).get("whitelist").getChildrenList()) {
            whitelist.add(UUID.fromString(uuid.getString()));
        }

        return whitelist;
    }

    @Override
    public List<Log> getLogsFromStorage() {
        List<Log> logs = new ArrayList<Log>();

        for (ConfigurationNode log : ((FileStorage) this.getStorage()).get("logs").getChildrenList()) {
            long timestamp = log.getLong();
            String player = log.getNode("player").getString();
            String mod = log.getNode("mod").getString();
            Action action = Action.fromString(log.getNode("action").getString());
            logs.add(new Log(timestamp, player, mod, action));
        }

        return logs;
    }

    @Override
    public void addToStorage(UUID uuid) {
        ((FileStorage) this.getStorage()).get("whitelist").getAppendedNode().setValue(uuid.toString());
        ((FileStorage) this.getStorage()).save();
    }

    @Override
    public void removeFromStorage(UUID uuid) {
        ((FileStorage) this.getStorage()).get("whitelist").getAppendedNode().removeChild(uuid.toString());
        ((FileStorage) this.getStorage()).save();
    }

    @Override
    public void logActionToStorage(Log log) {
        String timeStamp = String.valueOf(log.getTimestamp());
        ((FileStorage) this.getStorage()).get("logs").getAppendedNode().setValue(timeStamp);
        ((FileStorage) this.getStorage()).get("logs", timeStamp).getAppendedNode().setValue("player").setValue(log.getPlayerId());
        ((FileStorage) this.getStorage()).get("logs", timeStamp).getAppendedNode().setValue("mod").setValue(log.getModId());
        ((FileStorage) this.getStorage()).get("logs", timeStamp).getAppendedNode().setValue("action").setValue(log.getAction().toString());
        ((FileStorage) this.getStorage()).save();
    }
}
