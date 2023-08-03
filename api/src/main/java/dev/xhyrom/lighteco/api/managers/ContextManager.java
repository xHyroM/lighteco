package dev.xhyrom.lighteco.api.managers;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface ContextManager<T> {
    @NonNull UUID getPlayerUniqueId(@NonNull T context);
    @NonNull Class<?> getPlayerClass();
}