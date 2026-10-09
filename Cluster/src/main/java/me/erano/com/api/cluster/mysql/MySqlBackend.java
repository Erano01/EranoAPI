package me.erano.com.api.cluster.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import me.erano.com.api.cluster.ArenaStatus;
import me.erano.com.api.cluster.Backend;
import me.erano.com.api.cluster.Cluster;
import me.erano.com.api.cluster.ClusterException;
import me.erano.com.api.cluster.ClusterSettings;

/**
 * The network's state in two MySQL / MariaDB tables. {@code <prefix>arenas}: one row per arena, written by its
 * server; a row not written for {@link Cluster#FRESH_SECONDS} is of a server that's gone and isn't read.
 * {@code <prefix>joins}: one row per player on their way to an arena. Times are the database's own, so servers with
 * wrong clocks don't matter. Nothing is pushed: readers read every few seconds.
 */
public final class MySqlBackend implements Backend {

    /** Rows of servers gone this long are deleted. */
    private static final long FORGET_SECONDS = TimeUnit.DAYS.toSeconds(1);
    /** The columns of {@code <prefix>arenas}; an older table without them is made again (its rows are short-lived). */
    private static final String[] ARENA_COLUMNS = {"game", "server", "arena", "state", "stage", "paused",
            "seconds_left", "players", "spectators", "max_players", "joinable", "updated_at"};

    private final DataSource pool;
    private final AutoCloseable closer;
    private final String arenas;
    private final String joins;

    /** @param closer closes {@code pool}; {@code null} when the caller owns it */
    MySqlBackend(DataSource pool, AutoCloseable closer, String prefix) {
        this.pool = pool;
        this.closer = closer;
        this.arenas = prefix + "arenas";
        this.joins = prefix + "joins";
    }

    /** With a connection pool of its own. */
    public static MySqlBackend connect(ClusterSettings settings, Logger logger) throws ClusterException {
        HikariConfig config = new HikariConfig();
        config.setPoolName("EranoAPI-Cluster" + (settings.serverName().isEmpty() ? "" : " " + settings.serverName()));
        // Relocated in a plugin jar; the class reference follows the relocation, a written name wouldn't.
        config.setDriverClassName(org.mariadb.jdbc.Driver.class.getName());
        config.setJdbcUrl(settings.jdbcUrl());
        config.setUsername(settings.username());
        config.setPassword(settings.password());
        // Calls run one after another; the second connection covers a dropped one being replaced.
        config.setMaximumPoolSize(2);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(TimeUnit.SECONDS.toMillis(5));
        config.setMaxLifetime(TimeUnit.MINUTES.toMillis(30));
        config.setKeepaliveTime(TimeUnit.MINUTES.toMillis(5));
        HikariDataSource pool;
        try {
            pool = new HikariDataSource(config);
        } catch (RuntimeException e) {
            // HikariCP's PoolInitializationException wraps the driver's SQLException.
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            throw new ClusterException(settings.describe() + " can't be reached: " + cause.getMessage(), cause);
        }
        MySqlBackend backend = new MySqlBackend(pool, pool, settings.prefix());
        try {
            backend.createTables(logger);
        } catch (SQLException e) {
            pool.close();
            throw new ClusterException("couldn't make the tables in " + settings.describe() + ": " + e.getMessage(), e);
        }
        return backend;
    }

    /** On a pool the caller owns and closes (tests, a plugin's own pool). */
    public static MySqlBackend on(DataSource pool, String prefix, Logger logger) throws SQLException {
        MySqlBackend backend = new MySqlBackend(pool, null, prefix);
        backend.createTables(logger);
        return backend;
    }

    void createTables(Logger logger) throws SQLException {
        try (Connection connection = pool.getConnection()) {
            if (exists(connection, arenas) && !hasColumns(connection, arenas, ARENA_COLUMNS)) {
                // Only fresh rows matter and every server writes its own again within seconds.
                logger.info("Cluster: " + arenas + " is from an older version; made again");
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("DROP TABLE " + arenas);
                }
            }
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + arenas + " ("
                        + "game VARCHAR(64) NOT NULL, "
                        + "server VARCHAR(64) NOT NULL, "
                        + "arena VARCHAR(64) NOT NULL, "
                        + "state VARCHAR(64) NOT NULL, "
                        + "stage VARCHAR(16) NOT NULL, "
                        + "paused BOOLEAN NOT NULL, "
                        + "seconds_left INT NOT NULL, "
                        + "players INT NOT NULL, "
                        + "spectators INT NOT NULL, "
                        + "max_players INT NOT NULL, "
                        + "joinable BOOLEAN NOT NULL, "
                        + "updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                        + "PRIMARY KEY (server, arena))");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + joins + " ("
                        + "uuid CHAR(36) NOT NULL PRIMARY KEY, "
                        + "server VARCHAR(64) NOT NULL, "
                        + "arena VARCHAR(64) NOT NULL, "
                        + "created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            }
        }
    }

    private static boolean exists(Connection connection, String table) {
        try (Statement statement = connection.createStatement();
             ResultSet ignored = statement.executeQuery("SELECT 1 FROM " + table + " WHERE 1 = 0")) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private static boolean hasColumns(Connection connection, String table, String[] columns) throws SQLException {
        Set<String> have = new HashSet<>();
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM " + table + " WHERE 1 = 0")) {
            ResultSetMetaData meta = result.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                have.add(meta.getColumnLabel(i).toLowerCase(Locale.ROOT));
            }
        }
        for (String column : columns) {
            if (!have.contains(column)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void publish(String server, Collection<ArenaStatus> list) throws SQLException {
        try (Connection connection = pool.getConnection()) {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement("INSERT INTO " + arenas
                        + " (game, server, arena, state, stage, paused, seconds_left, players, spectators, max_players,"
                        + " joinable, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)"
                        + " ON DUPLICATE KEY UPDATE game = ?, state = ?, stage = ?, paused = ?, seconds_left = ?,"
                        + " players = ?, spectators = ?, max_players = ?, joinable = ?,"
                        + " updated_at = CURRENT_TIMESTAMP")) {
                    for (ArenaStatus arena : list) {
                        statement.setString(1, arena.game());
                        statement.setString(2, server);
                        statement.setString(3, arena.arena());
                        statement.setString(4, arena.state());
                        statement.setString(5, arena.stage().name());
                        statement.setBoolean(6, arena.paused());
                        statement.setInt(7, arena.secondsLeft());
                        statement.setInt(8, arena.players());
                        statement.setInt(9, arena.spectators());
                        statement.setInt(10, arena.maxPlayers());
                        statement.setBoolean(11, arena.joinable());
                        statement.setString(12, arena.game());
                        statement.setString(13, arena.state());
                        statement.setString(14, arena.stage().name());
                        statement.setBoolean(15, arena.paused());
                        statement.setInt(16, arena.secondsLeft());
                        statement.setInt(17, arena.players());
                        statement.setInt(18, arena.spectators());
                        statement.setInt(19, arena.maxPlayers());
                        statement.setBoolean(20, arena.joinable());
                        statement.addBatch();
                    }
                    if (!list.isEmpty()) {
                        statement.executeBatch();
                    }
                }
                List<String> gone = new ArrayList<>();
                try (PreparedStatement statement = connection.prepareStatement(
                        "SELECT arena FROM " + arenas + " WHERE server = ?")) {
                    statement.setString(1, server);
                    try (ResultSet result = statement.executeQuery()) {
                        while (result.next()) {
                            gone.add(result.getString(1));
                        }
                    }
                }
                for (ArenaStatus arena : list) {
                    gone.remove(arena.arena());
                }
                if (!gone.isEmpty()) {
                    try (PreparedStatement statement = connection.prepareStatement(
                            "DELETE FROM " + arenas + " WHERE server = ? AND arena = ?")) {
                        for (String arena : gone) {
                            statement.setString(1, server);
                            statement.setString(2, arena);
                            statement.addBatch();
                        }
                        statement.executeBatch();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(autoCommit);
            }
        }
    }

    @Override
    public void remove(String server) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM " + arenas + " WHERE server = ?")) {
            statement.setString(1, server);
            statement.executeUpdate();
        }
    }

    @Override
    public List<ArenaStatus> arenas() throws SQLException {
        List<ArenaStatus> list = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT game, server, arena, state, stage,"
                     + " paused, seconds_left, players, spectators, max_players, joinable FROM " + arenas
                     + " WHERE updated_at >= ? ORDER BY server, arena")) {
            statement.setTimestamp(1, ago(connection, Cluster.FRESH_SECONDS));
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    ArenaStatus.Stage stage;
                    try {
                        stage = ArenaStatus.Stage.valueOf(result.getString(5));
                    } catch (IllegalArgumentException e) {
                        // A stage a newer version wrote: nothing can be done there as far as this one knows.
                        stage = ArenaStatus.Stage.CLOSED;
                    }
                    list.add(new ArenaStatus(result.getString(1), result.getString(2), result.getString(3),
                            result.getString(4), stage, result.getBoolean(6), result.getInt(7), result.getInt(8),
                            result.getInt(9), result.getInt(10), result.getBoolean(11)));
                }
            }
        }
        return list;
    }

    @Override
    public boolean isWritten(String server) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT 1 FROM " + arenas + " WHERE server = ? AND updated_at >= ?")) {
            statement.setString(1, server);
            statement.setTimestamp(2, ago(connection, Cluster.FRESH_SECONDS));
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    @Override
    public void forgetGone() throws SQLException {
        try (Connection connection = pool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + arenas + " WHERE updated_at < ?")) {
                statement.setTimestamp(1, ago(connection, FORGET_SECONDS));
                statement.executeUpdate();
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + joins + " WHERE created_at < ?")) {
                statement.setTimestamp(1, ago(connection, FORGET_SECONDS));
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void sendTo(UUID player, String server, String arena) throws SQLException {
        try (Connection connection = pool.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO " + joins
                     + " (uuid, server, arena, created_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)"
                     + " ON DUPLICATE KEY UPDATE server = ?, arena = ?, created_at = CURRENT_TIMESTAMP")) {
            statement.setString(1, player.toString());
            statement.setString(2, server);
            statement.setString(3, arena);
            statement.setString(4, server);
            statement.setString(5, arena);
            statement.executeUpdate();
        }
    }

    @Override
    public String arriving(UUID player, String server) throws SQLException {
        String arena = null;
        try (Connection connection = pool.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT arena FROM " + joins + " WHERE uuid = ? AND server = ? AND created_at >= ?")) {
                statement.setString(1, player.toString());
                statement.setString(2, server);
                statement.setTimestamp(3, ago(connection, Cluster.JOIN_SECONDS));
                try (ResultSet result = statement.executeQuery()) {
                    if (result.next()) {
                        arena = result.getString(1);
                    }
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + joins + " WHERE uuid = ?")) {
                statement.setString(1, player.toString());
                statement.executeUpdate();
            }
        }
        return arena;
    }

    @Override
    public boolean listen(ChangeListener changed) {
        return false;
    }

    @Override
    public void close() {
        if (closer != null) {
            try {
                closer.close();
            } catch (Exception ignored) {
                // Closing anyway.
            }
        }
    }

    /** The database's time {@code seconds} ago. */
    private static Timestamp ago(Connection connection, long seconds) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT CURRENT_TIMESTAMP")) {
            result.next();
            return new Timestamp(result.getTimestamp(1).getTime() - seconds * 1000L);
        }
    }
}
