package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.manager.CurrencyManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public class ApiCurrencyManager extends ApiAbstractManager<dev.xhyrom.lighteco.common.manager.currency.CurrencyManager> implements CurrencyManager {
    public ApiCurrencyManager(LightEcoPlugin plugin, dev.xhyrom.lighteco.common.manager.currency.CurrencyManager handler) {
        super(plugin, handler);
    }

    private Currency wrap(dev.xhyrom.lighteco.common.model.currency.Currency handler) {
        return handler.getProxy();
    }

    @Override
    public @NonNull Collection<Currency> getRegisteredCurrencies() {
        return this.handler.values()
                .stream().map(this::wrap)
                .toList();
    }

    @Override
    public Currency getCurrency(@NonNull String identifier) {
        return wrap(this.handler.getIfLoaded(identifier));
    }

    @Override
    public void registerCurrency(@NonNull Currency currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency internal = new dev.xhyrom.lighteco.common.model.currency.Currency(currency);
        this.handler.registerCurrency(internal);
    }
}
