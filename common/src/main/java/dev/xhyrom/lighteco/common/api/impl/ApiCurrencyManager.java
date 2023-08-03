package dev.xhyrom.lighteco.common.api.impl;

import dev.xhyrom.lighteco.api.managers.CurrencyManager;
import dev.xhyrom.lighteco.api.model.currency.Currency;
import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ApiCurrencyManager extends ApiAbstractManager<dev.xhyrom.lighteco.common.managers.currency.CurrencyManager> implements CurrencyManager {
    public ApiCurrencyManager(LightEcoPlugin plugin, dev.xhyrom.lighteco.common.managers.currency.CurrencyManager handler) {
        super(plugin, handler);
    }

    private Currency<?> wrap(dev.xhyrom.lighteco.common.model.currency.Currency<?> handler) {
        return handler.getProxy();
    }

    @Override
    public @NonNull Collection<Currency<?>> getRegisteredCurrencies() {
        return this.handler.values()
                .stream().map(this::wrap)
                .collect(Collectors.toList());
    }

    @Override
    public <T> Currency<T> getCurrency(@NonNull String identifier) {
        return (Currency<T>) wrap(this.handler.getIfLoaded(identifier));
    }

    @Override
    public void registerCurrency(@NonNull Currency<?> currency) {
        dev.xhyrom.lighteco.common.model.currency.Currency<?> internal = new dev.xhyrom.lighteco.common.model.currency.Currency<>(plugin, currency);
        this.handler.registerCurrency(internal);
    }

    @Override
    public List<User> getTopUsers(@NonNull Currency<?> currency, int length) {
        dev.xhyrom.lighteco.common.model.currency.Currency<?> internal = this.handler.getIfLoaded(currency.getIdentifier());
        return this.handler.getTopUsers(internal, length)
                .stream().map(dev.xhyrom.lighteco.common.model.user.User::getProxy)
                .collect(Collectors.toList());
    }
}