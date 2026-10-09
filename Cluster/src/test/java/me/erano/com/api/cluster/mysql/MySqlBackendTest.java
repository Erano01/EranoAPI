package me.erano.com.api.cluster.mysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Collections;
import java.util.logging.Logger;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import me.erano.com.api.cluster.BackendContract;
import me.erano.com.api.cluster.QuickJoinTest;

class MySqlBackendTest {

    private static final Logger LOGGER = Logger.getLogger("test");

    private static JdbcDataSource database(String name) {
        JdbcDataSource source = new JdbcDataSource();
        source.setURL("jdbc:h2:mem:" + name + ";MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1");
        return source;
    }

    @Test
    void keepsTheContract() throws Exception {
        MySqlBackend backend = MySqlBackend.on(database("contract"), "hg_", LOGGER);
        BackendContract.check(backend);
        assertFalse(backend.listen(server -> { }));
    }

    @Test
    void anArenasTableFromBeforeGamesAndStagesIsMadeAgain() throws Exception {
        JdbcDataSource source = database("old");
        try (Connection connection = source.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE hg_arenas (server VARCHAR(64) NOT NULL, arena VARCHAR(64) NOT NULL,"
                    + " state VARCHAR(32) NOT NULL, paused BOOLEAN NOT NULL, seconds_left INT NOT NULL,"
                    + " players INT NOT NULL, spectators INT NOT NULL, max_players INT NOT NULL,"
                    + " joinable BOOLEAN NOT NULL, updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + " PRIMARY KEY (server, arena))");
            statement.executeUpdate("INSERT INTO hg_arenas (server, arena, state, paused, seconds_left, players,"
                    + " spectators, max_players, joinable)"
                    + " VALUES ('hg-1', 'breeze', 'BATTLE', FALSE, 1, 1, 1, 1, FALSE)");
        }
        MySqlBackend backend = MySqlBackend.on(source, "hg_", LOGGER);
        assertEquals(Collections.emptyList(), backend.arenas());
        backend.publish("hg-1", Collections.singletonList(QuickJoinTest.arena("hg-1", "breeze", 1, 1, true)));
        assertEquals(1, backend.arenas().size());
    }
}
