package dev.xhyrom.lighteco.common.manager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ConcurrentManager<I, T> implements Manager<I, T> {
    public final Map<I, T> map = new ConcurrentHashMap<>();

    @Override
    public Collection<I> keys() {
        return this.map.keySet();
    }

    @Override
    public Collection<T> values() {
        return this.map.values();
    }

    @Override
    public T getOrMake(I identifier) {
        return this.map.computeIfAbsent(identifier, this::apply);
    }

    @Override
    public T getIfLoaded(I identifier) {
        return this.map.get(identifier);
    }

    @Override
    public boolean isLoaded(I identifier) {
        return this.map.containsKey(identifier);
    }

    @Override
    public void unload(I identifier) {
        this.map.remove(identifier);
    }
}
