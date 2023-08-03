package dev.xhyrom.lighteco.common.storage;

import dev.xhyrom.lighteco.api.model.user.User;
import dev.xhyrom.lighteco.api.storage.StorageProvider;
import dev.xhyrom.lighteco.common.plugin.LightEcoPlugin;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class Storage {
    private final LightEcoPlugin plugin;
    private final StorageProvider provider;

    public Storage(LightEcoPlugin plugin, StorageProvider provider) {
        this.plugin = plugin;
        this.provider = provider;
    }

    private <T> CompletableFuture<T> future(Callable<T> callable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return callable.call();
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }

                throw new CompletionException(e);
            }
        });
    }

    private CompletableFuture<Void> future(Runnable runnable) {
        return CompletableFuture.runAsync(runnable);
    }

    public CompletableFuture<User> loadUser(UUID uniqueId) {
        return future(() -> this.provider.loadUser(uniqueId));
    }

    public CompletableFuture<Void> saveUser(User user) {
        return future(() -> this.provider.saveUser(user));
    }
}