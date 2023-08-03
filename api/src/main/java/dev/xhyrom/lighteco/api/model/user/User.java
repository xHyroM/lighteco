package dev.xhyrom.lighteco.api.model.user;

import dev.xhyrom.lighteco.api.model.currency.Currency;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface User {
    /**
     * Get the unique id of this user.
     *
     * @return the unique id
     */
    @NonNull UUID getUniqueId();

    /**
     * Get the username of this user.
     *
     * @return the username
     */
    @NonNull String getUsername();

    /**
     * Get the balance of this user for the specified currency.
     *
     * @param currency the currency
     * @return the balance
     */
    <T> T getBalance(@NonNull Currency<?> currency);

    /**
     * Set the balance of this user for the specified currency.
     *
     * @param currency the currency
     * @param balance the balance
     */
    <T> void setBalance(@NonNull Currency<?> currency, @NonNull T balance);
}