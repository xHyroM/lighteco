package dev.xhyrom.lighteco.bukkit.manager;

import dev.jorel.commandapi.CommandAPICommand;
import dev.xhyrom.lighteco.bukkit.commands.*;
import dev.xhyrom.lighteco.common.manager.command.AbstractCommandManager;
import dev.xhyrom.lighteco.common.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

public class BukkitCommandManager extends AbstractCommandManager {
    public final BukkitAudiences audienceFactory;

    public BukkitCommandManager(LightEcoPlugin plugin) {
        super(plugin);

        this.audienceFactory = BukkitAudiences.create((JavaPlugin) this.plugin.getBootstrap().getLoader());
    }

    @Override
    public void registerCurrencyCommand(@NonNull Currency currency) {
        registerCommands(currency.getIdentifier(), currency);

        for (String alias : currency.getIdentifierAliases()) {
            registerCommands(alias, currency);
        }
    }

    @Override
    public void registerCurrencyCommand(@NonNull Currency currency, boolean main) {
        if (!main) {
            registerCurrencyCommand(currency);
            return;
        }

        String permissionBase = "lighteco.currency." + currency.getIdentifier() + ".command.";

        // Register main command
        registerCurrencyCommand(currency);

        // Expose pay as main command
        if (currency.isPayable())
            new PayCommand(this, currency, permissionBase).build().register();

        // Expose balance as main command
        for (CommandAPICommand cmd : new BalanceCommand(
                this,
                "balance",
                currency,
                permissionBase
        ).multipleBuild()) {
            cmd.register();
        }
    }

    private void registerCommands(@NonNull String name, @NonNull Currency currency) {
        String permissionBase = "lighteco.currency." + currency.getIdentifier() + ".command.";

        // Balance
        for (CommandAPICommand cmd : new BalanceCommand(
                this,
                name,
                currency,
                permissionBase
        ).multipleBuild()) {
            cmd.register();
        }

        CommandAPICommand cmd = new CommandAPICommand(name)
                .withSubcommand(new SetCommand(this, currency, permissionBase).build())
                .withSubcommand(new GiveCommand(this, currency, permissionBase).build())
                .withSubcommand(new TakeCommand(this, currency, permissionBase).build())
                .withSubcommands(new BalanceCommand(this, "balance", currency, permissionBase).multipleBuild());

        if (currency.isPayable())
            cmd = cmd.withSubcommand(new PayCommand(this, currency, permissionBase).build());

        cmd.register();
    }
}
