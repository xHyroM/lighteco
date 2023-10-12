package dev.xhyrom.lighteco.common.plugin.bootstrap;

import dev.xhyrom.lighteco.common.plugin.logger.PluginLogger;
import dev.xhyrom.lighteco.common.plugin.scheduler.SchedulerAdapter;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LightEcoBootstrap {
    Object getLoader();
    PluginLogger getLogger();
    SchedulerAdapter getScheduler();
    Path getDataDirectory();

    Optional<UUID> lookupUniqueId(String username);
    boolean isPlayerOnline(UUID uniqueId);
    List<UUID> getOnlinePlayers();

    InputStream getResourceStream(String filename);
}
