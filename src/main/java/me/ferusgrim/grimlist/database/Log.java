package me.ferusgrim.grimlist.database;

public class Log {

    private final long timestamp;
    private final String playerId;
    private final String modId;
    private final Action action;

    public Log(long timestamp, String playerId, String modId, Action action) {
        this.timestamp = timestamp;
        this.playerId = playerId;
        this.modId = modId;
        this.action = action;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getModId() {
        return this.modId;
    }

    public Action getAction() {
        return this.action;
    }

    public boolean wasRemoved() {
        return this.action == Action.REMOVED;
    }

    public boolean wasAdded() {
        return this.action == Action.ADDED;
    }
}
