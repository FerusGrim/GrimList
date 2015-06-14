package me.ferusgrim.grimlist.storage;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;

public class HoconFileStorage extends FileStorage {

    private ConfigurationNode root;
    private ConfigurationLoader<CommentedConfigurationNode> loader;

    public HoconFileStorage(File configDir, String fileName, String defaultPath) {
        super(configDir, fileName, defaultPath);
    }

    public HoconFileStorage(File configDir, String fileName) {
        this(configDir, fileName, "");
    }

    @Override
    protected ConfigurationLoader<CommentedConfigurationNode> createLoader() {
        return HoconConfigurationLoader.builder().setFile(this.getStorageFile()).build();
    }

    @Override
    protected ConfigurationLoader<CommentedConfigurationNode> createDefaultLoader(String defaultPath) {
        return HoconConfigurationLoader.builder().setURL(this.getClass().getResource(defaultPath)).build();
    }
}
