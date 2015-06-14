package me.ferusgrim.grimlist.command;

import com.sk89q.squirrelid.Profile;
import me.ferusgrim.grimlist.GrimList;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.spec.CommandExecutor;

import java.util.UUID;

public class RemoveCmd implements CommandExecutor {

    private final GrimList plugin;

    public RemoveCmd(GrimList plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(final CommandSource src, CommandContext args) throws CommandException {
        final String mod = this.getModClassification(src);

        final String playerName = args.<String>getOne("player").get();
        src.sendMessage(Texts.of("Attempting to remove " + playerName + " from the whitelist!"));

        this.plugin.getGame().getAsyncScheduler().runTask(this.plugin, new Runnable() {
            @Override
            public void run() {
                UUID playerId = this.getPlayerId(playerName);

                if (playerId == null) {
                    this.sendMessage("A player by that name couldn't be found on the Minecraft servers.");
                    return;
                }

                this.sendMessage("UUID (" + playerName + "): " + playerId.toString());

                if (!plugin.getDatabase().isWhitelisted(playerId)) {
                    this.sendMessage("Player isn't on the whitelist!");
                } else {
                    plugin.getDatabase().remove(playerId, mod);
                    this.sendMessage(playerName + " has been removed from the whitelist!");
                }
            }

            private void sendMessage(String message) {
                if (src instanceof Player) {
                    if (((Player) src).isOnline()) {
                        src.sendMessage(Texts.of(message));
                    }
                } else {
                    src.sendMessage(Texts.of(message));
                }
            }

            private UUID getPlayerId(String playerName) {
                try {
                    Profile player = plugin.getResolver().findByName(playerName);
                    if (player != null) {
                        return player.getUniqueId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        return CommandResult.success();
    }

    public String getModClassification(CommandSource src) {
        return src instanceof Player ? ((Player) src).getUniqueId().toString() : src.getName();
    }
}
