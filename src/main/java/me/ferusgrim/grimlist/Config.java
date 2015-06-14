package me.ferusgrim.grimlist;

import me.ferusgrim.grimlist.database.DatabaseType;
import me.ferusgrim.grimlist.storage.HoconFileStorage;
import ninja.leaping.configurate.ConfigurationNode;

import java.io.File;

public class Config extends HoconFileStorage {

    public static final String defaultPath = "/me/ferusgrim/grimlist/defaultConfig.conf";

    public Config(File configDir) {
        super(configDir, "config.conf", Config.defaultPath);
    }

    private ConfigurationNode getDatabase() {
        return this.get("database");
    }

    private ConfigurationNode getHocon() {
        return this.getDatabase().getNode("hocon");
    }

    private ConfigurationNode getMysql() {
        return this.getDatabase().getNode("mysql");
    }

    public boolean isWhitelistEnabled() {
        return this.get("enable-whitelist").getBoolean();
    }

    public long getUpdateInterval() {
        return this.get("update-interval").getLong();
    }

    public DatabaseType getDatabaseType() {
        return DatabaseType.fromString(this.getDatabase().getNode("type").getString());
    }

    public String getHoconFilename() {
        return this.getHocon().getNode("file-name").getString();
    }

    public String getMysqlHost() {
        return this.getMysql().getNode("host").getString();
    }

    public int getMysqlPort() {
        return this.getMysql().getNode("port").getInt();
    }

    public String getMysqlDatabase() {
        return this.getMysql().getNode("database").getString();
    }

    public String getMysqlUsername() {
        return this.getMysql().getNode("username").getString();
    }

    public String getMysqlPassword() {
        return this.getMysql().getNode("password").getString();
    }
}
