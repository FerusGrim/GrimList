package me.ferusgrim.grimlist;

import com.google.inject.Inject;
import com.sk89q.squirrelid.cache.HashMapCache;
import com.sk89q.squirrelid.resolver.CacheForwardingService;
import com.sk89q.squirrelid.resolver.HttpRepositoryService;
import me.ferusgrim.grimlist.command.AddCmd;
import me.ferusgrim.grimlist.command.RemoveCmd;
import me.ferusgrim.grimlist.database.Database;
import me.ferusgrim.grimlist.database.HoconDatabase;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Subscribe;
import org.spongepowered.api.event.state.InitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.ProviderExistsException;
import org.spongepowered.api.service.config.ConfigDir;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.args.GenericArguments;
import org.spongepowered.api.util.command.spec.CommandSpec;

import java.io.File;

@Plugin(id = GrimList.ID, name = GrimList.ID, version = GrimList.VERSION)
public class GrimList {

    public static final String ID = "GrimList";
    public static final String VERSION = "1.0-SNAPSHOT";

    @Inject private Game game;
    @Inject private Logger logger;
    @Inject @ConfigDir(sharedRoot = false) private File configDir;

    private Config config;
    private Database database;
    private CacheForwardingService resolver;

    @Subscribe public void onInit(InitializationEvent event) {
        this.config = new Config(this.configDir);
        this.database = this.determineDatabase();

        this.setupResolver();
        this.setupCommands();
    }

    public Game  getGame() {
        return this.game;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public File getConfigDir() {
        return this.configDir;
    }

    public Config getConfig() {
        return this.config;
    }

    public Database getDatabase() {
        return this.database;
    }

    public CacheForwardingService getResolver() {
        return this.resolver;
    }

    private Database determineDatabase() {
        switch (this.config.getDatabaseType()) {
            case HOCON:
            case MYSQL:
            default:
                return new HoconDatabase(this);
        }
    }

    private void setupResolver() {
        this.resolver = new CacheForwardingService(HttpRepositoryService.forMinecraft(), new HashMapCache());
        try {
            this.getGame().getServiceManager().setProvider(this, CacheForwardingService.class, this.resolver);
        } catch (ProviderExistsException e) {
            this.resolver = this.getGame().getServiceManager().provide(CacheForwardingService.class).get();
        }
    }

    private void setupCommands() {
        CommandSpec addCmd = CommandSpec.builder()
                .description(Texts.of("Adds a specified player to the whitelist."))
                .permission("grimlist.add")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("player"))))
                .executor(new AddCmd(this))
                .build();

        CommandSpec removeCmd = CommandSpec.builder()
                .description(Texts.of("Removes a specified player from the whitelist."))
                .permission("grimlist.remove")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Texts.of("player"))))
                .executor(new RemoveCmd(this))
                .build();

        CommandSpec whitelistCmd = CommandSpec.builder()
                .description(Texts.of("Main command used in GrimList."))
                .child(addCmd, "add", "-a")
                .child(removeCmd, "remove", "-r")
                .build();

        this.game.getCommandDispatcher().register(this, whitelistCmd, "whitelist", "grimlist");
    }
}
