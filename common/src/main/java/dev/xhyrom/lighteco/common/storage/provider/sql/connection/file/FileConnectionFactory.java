package dev.xhyrom.lighteco.common.storage.provider.sql.connection.file;

import dev.xhyrom.lighteco.common.storage.provider.sql.connection.ConnectionFactory;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

abstract class FileConnectionFactory implements ConnectionFactory {
    private Connection connection;
    private final Path file;

    protected FileConnectionFactory(Path file) {
        this.file = file;
    }

    protected abstract Connection createConnection(Path file) throws SQLException;

    @Override
    public void shutdown() throws Exception {
        if (this.connection == null) return;

        this.connection.close();
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = this.connection;
        if (connection == null || connection.isClosed()) {
            connection = createConnection(this.file);
            this.connection = connection;
        }

        return connection;
    }
}
