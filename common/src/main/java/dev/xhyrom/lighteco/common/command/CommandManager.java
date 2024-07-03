package dev.xhyrom.lighteco.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.xhyrom.lighteco.common.command.abstraction.Command;
import dev.xhyrom.lighteco.common.commands.BalanceCommand;
import dev.xhyrom.lighteco.common.commands.CurrencyParentCommand;
import dev.xhyrom.lighteco.common.commands.InfoCommand;
import dev.xhyrom.lighteco.common.commands.PayCommand;
import dev.xhyrom.lighteco.common.model.chat.CommandSender;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class CommandManager {
    protected final LightEcoPlugin plugin;
    @Getter
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();

    public CommandManager(LightEcoPlugin plugin) {
        this.plugin = plugin;
    }

    // Register built-in commands
    public void register() {
        register(new InfoCommand());
    }

    public void register(Currency currency, boolean main) {
        register(new CurrencyParentCommand(currency));

        if (main) {
            register(BalanceCommand.create(currency));
            register(PayCommand.create(currency));
        }
    }

    protected void register(Command command) {
        dispatcher.getRoot().addChild(command.build());
    }

    public void execute(CommandSender sender, String name, String[] args) {
        final CommandSource source = new CommandSource(this.plugin, sender);
        final ParseResults<CommandSource> parseResults = dispatcher.parse(
                name + (args.length > 0 ? " " + String.join(" ", args) : ""),
                source
        );

        try {
            dispatcher.execute(parseResults);
        } catch (CommandSyntaxException e) {
            sender.sendMessage(Component.text(e.getRawMessage().getString(), NamedTextColor.RED));

            if (e.getInput() != null && e.getCursor() >= 0) {
                int j = Math.min(e.getInput().length(), e.getCursor());

                Component msg = Component.empty().color(NamedTextColor.GRAY).clickEvent(ClickEvent.clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + name));

                if (j > 10) {
                    msg = msg.append(Component.text("..."));
                }

                msg = msg.append(Component.text(e.getInput().substring(Math.max(0, j - 10), j)));

                if (j < e.getInput().length()) {
                    Component component = Component.text(e.getInput().substring(j)).color(NamedTextColor.RED).decorate(TextDecoration.UNDERLINED);
                    msg = msg.append(component);
                }

                msg = msg.append(Component.translatable("command.context.here").color(NamedTextColor.RED).decorate(TextDecoration.ITALIC));
                sender.sendMessage(msg);
            }
        }
    }
}
