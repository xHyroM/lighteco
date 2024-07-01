package dev.xhyrom.lighteco.bukkit.listeners;

import com.destroystokyo.paper.event.brigadier.AsyncPlayerSendCommandsEvent;
import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.bukkit.BukkitLightEcoPlugin;
import dev.xhyrom.lighteco.bukkit.brigadier.BukkitBrigadier;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.common.command.CommandSource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BukkitCommandSuggestionsListener implements Listener {
    private final BukkitLightEcoPlugin plugin;

    public BukkitCommandSuggestionsListener(BukkitLightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
    @EventHandler
    public void onPlayerSendCommandsEvent(AsyncPlayerSendCommandsEvent<?> event) {
        BukkitCommandSender sender = new BukkitCommandSender(event.getPlayer(), this.plugin.getCommandManager().audienceFactory);
        CommandSource source = new CommandSource(this.plugin, sender);
        if (event.isAsynchronous() || !event.hasFiredAsync()) {
            for (CommandNode<CommandSource> command : this.plugin.getCommandManager().getDispatcher().getRoot().getChildren()) {
                if (!command.canUse(source)) continue;

                BukkitBrigadier.removeChild(event.getCommandNode(), command.getName());
                event.getCommandNode().addChild((CommandNode) command);
            }
        }
    }
}
