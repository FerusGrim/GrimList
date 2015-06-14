package me.ferusgrim.grimlist.database;

import me.ferusgrim.grimlist.GrimList;
import me.ferusgrim.grimlist.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Database {

    private final GrimList plugin;
    private final DatabaseType databaseType;
    private final Storage storage;

    private final List<UUID> whiteList;
    private final List<Log> logs;

    public Database(GrimList plugin, DatabaseType databaseType, Storage storage) {
        this.plugin = plugin;
        this.databaseType = databaseType;
        this.storage = storage;

        plugin.getLogger().info("Grabbing whitelist from storage.");
        this.whiteList = this.getWhitelistFromStorage();

        plugin.getLogger().info("Grabbing logs from storage.");
        this.logs = this.getLogsFromStorage();
    }

    protected GrimList getPlugin() {
        return this.plugin;
    }

    public DatabaseType getType() {
        return this.databaseType;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public List<UUID> getWhiteList() {
        return this.whiteList;
    }

    public boolean isWhitelisted(UUID uuid) {
        return this.whiteList.contains(uuid);
    }

    public List<Log> getLogs() {
        return this.logs;
    }

    public List<Log> getLogsByPlayer(UUID uuid) {
        List<Log> logs = new ArrayList<Log>();
        for (Log log : this.logs) {
            if (log.getPlayerId().equals(uuid)) {
                logs.add(log);
            }
        }
        return logs;
    }

    public List<Log> getLogsByMod(UUID uuid) {
        List<Log> logs = new ArrayList<Log>();
        for (Log log : this.logs) {
            if (log.getModId().equalsIgnoreCase(uuid.toString())) {
                logs.add(log);
            }
        }
        return logs;
    }

    public void add(final UUID player, String mod) {
        final Log log = new Log(System.currentTimeMillis(), player.toString(), mod, Action.ADDED);

        this.whiteList.add(player);
        this.logs.add(log);

        this.plugin.getGame().getAsyncScheduler().runTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                addToStorage(player);
                //logActionToStorage(log);
            }
        });
    }

    public void remove(final UUID player, String mod) {
        final Log log = new Log(System.currentTimeMillis(), player.toString(), mod, Action.REMOVED);

        this.whiteList.remove(player);
        this.logs.add(log);

        this.plugin.getGame().getAsyncScheduler().runTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                removeFromStorage(player);
                logActionToStorage(log);
            }
        });
    }

    public abstract List<UUID> getWhitelistFromStorage();
    public abstract List<Log> getLogsFromStorage();

    public abstract void addToStorage(UUID uuid);
    public abstract void removeFromStorage(UUID uuid);
    public abstract void logActionToStorage(Log log);
}
