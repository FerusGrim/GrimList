package me.ferusgrim.grimlist.storage;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public abstract class FileStorage implements Storage {

    private final File storageFile;
    private final ConfigurationNode root;
    private final ConfigurationLoader<CommentedConfigurationNode> loader;

    public FileStorage(File configDir, String fileName, String defaultPath) {
        this.storageFile = new File(configDir, fileName);

        this.loader = this.createLoader();
        this.root = this.createRoot(defaultPath);
    }

    public FileStorage(File configDir, String fileName) {
        this(configDir, fileName, "");
    }

    public File getStorageFile() {
        return this.storageFile;
    }

    public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
        return this.loader;
    }

    private ConfigurationNode createRoot(String defaultPath) {
        if (!this.getStorageFile().exists()) {
            this.getStorageFile().getParentFile().mkdirs();
        }

        ConfigurationNode root = null;
        try {
            root = this.loader.load();
            if (!defaultPath.isEmpty()) {
                root.mergeValuesFrom(this.createDefaultLoader(defaultPath).load());
                this.loader.save(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public ConfigurationNode get(String... path) {
        return this.root.getNode(path);
    }

    public void save() {
        try {
            this.loader.save(this.root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract ConfigurationLoader<CommentedConfigurationNode> createLoader();

    protected abstract ConfigurationLoader<CommentedConfigurationNode> createDefaultLoader(String defaultPath);
}
