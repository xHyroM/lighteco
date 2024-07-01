package dev.xhyrom.lighteco.bukkit.manager;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.SuggestionContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import dev.xhyrom.lighteco.bukkit.chat.BukkitCommandSender;
import dev.xhyrom.lighteco.common.command.CommandManager;
import dev.xhyrom.lighteco.common.command.CommandSource;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class BukkitCommandManager extends CommandManager {
    public final BukkitAudiences audienceFactory;
    private CommandMap commandMap;

    public BukkitCommandManager(LightEcoPlugin plugin) {
        super(plugin);

        this.audienceFactory = BukkitAudiences.create((JavaPlugin) this.plugin.getBootstrap().getLoader());

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            field.setAccessible(true);
            this.commandMap = (CommandMap) field.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(Currency currency, boolean main) {
        super.register(currency, main);

        this.registerToBukkit(currency.getIdentifier());

        if (main) {
            this.registerToBukkit("balance");
            this.registerToBukkit("pay");
        }
    }

    private void registerToBukkit(String name) {
        Command command = new Command(name) {
            private final CommandNode<CommandSource> node = getDispatcher().getRoot().getChild(name);

            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                bukkitCommandManagerExecute(new BukkitCommandSender(commandSender, audienceFactory), s, strings);
                return true;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                final List<String> suggestions = new ArrayList<>();
                final CommandSource source = new CommandSource(plugin, new BukkitCommandSender(sender, audienceFactory));

                final ParseResults<CommandSource> parseResults = getDispatcher().parse(
                        name + (args.length > 0 ? " " + String.join(" ", args) : ""),
                        source
                );

                getDispatcher().getCompletionSuggestions(
                        parseResults,
                        parseResults.getReader().getTotalLength()
                ).join().getList().forEach(suggestion -> suggestions.add(suggestion.getText()));

                return suggestions;
            }
        };

        commandMap.register(name, command);
    }

    private void bukkitCommandManagerExecute(dev.xhyrom.lighteco.common.model.chat.CommandSender sender, String name, String[] args) {
        super.execute(sender, name, args);
    }
}
