package me.erano.com.api.cluster;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Where the network's state is kept and how to reach it. Every server of a network uses the same. */
public final class ClusterSettings {

    public enum Backend {
        /**
         * Tables in a MySQL / MariaDB database ({@code <prefix>arenas}, {@code <prefix>joins}). Nothing more to set up
         * for a network that already shares a database; servers and hubs read every few seconds.
         */
        MYSQL,
        /**
         * Keys in Redis ({@code <prefix>...}). Changes reach everyone at once (publish / subscribe) and the database
         * isn't touched every few seconds; for large networks.
         */
        REDIS
    }

    /** Letters, digits and {@code _ : -} only. */
    private static final String PREFIX_CHARACTERS = "[A-Za-z0-9_:-]*";

    private final Backend backend;
    private final String prefix;
    private final String serverName;
    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String redisHost;
    private final int redisPort;
    private final String redisUser;
    private final String redisPassword;
    private final int redisDatabase;
    private final boolean redisSsl;

    private ClusterSettings(Builder builder) {
        this.backend = builder.backend;
        this.prefix = builder.prefix;
        this.serverName = builder.serverName;
        this.jdbcUrl = builder.jdbcUrl;
        this.username = builder.username;
        this.password = builder.password;
        this.redisHost = builder.redisHost;
        this.redisPort = builder.redisPort;
        this.redisUser = builder.redisUser;
        this.redisPassword = builder.redisPassword;
        this.redisDatabase = builder.redisDatabase;
        this.redisSsl = builder.redisSsl;
    }

    /**
     * MySQL / MariaDB.
     *
     * @param jdbcUrl  {@code jdbc:mariadb://host:port/database?options} (the MariaDB driver talks to MySQL too)
     * @param prefix   before the table names, e.g. {@code hg_}: {@code hg_arenas}, {@code hg_joins}
     */
    public static Builder mysql(String jdbcUrl, String username, String password, String prefix) {
        Builder builder = new Builder(Backend.MYSQL, prefix);
        builder.jdbcUrl = require("jdbcUrl", jdbcUrl);
        builder.username = username == null ? "" : username;
        builder.password = password == null ? "" : password;
        return builder;
    }

    /** @param prefix before the key names, e.g. {@code hg:}: {@code hg:servers}, {@code hg:join:<uuid>} */
    public static Builder redis(String host, int port, String prefix) {
        Builder builder = new Builder(Backend.REDIS, prefix);
        builder.redisHost = require("host", host);
        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port must be 1 - 65535: " + port);
        }
        builder.redisPort = port;
        return builder;
    }

    /** The JDBC URL of a MySQL / MariaDB database; {@code properties} are the driver's options. */
    public static String jdbcUrl(String host, int port, String database, Map<String, String> properties) {
        StringBuilder url = new StringBuilder("jdbc:mariadb://").append(host).append(':').append(port).append('/')
                .append(database);
        char separator = '?';
        Map<String, String> options = properties == null ? Collections.<String, String>emptyMap()
                : new LinkedHashMap<>(properties);
        for (Map.Entry<String, String> option : options.entrySet()) {
            url.append(separator).append(option.getKey()).append('=').append(option.getValue());
            separator = '&';
        }
        return url.toString();
    }

    private static String require(String what, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(what + " is empty");
        }
        return value.trim();
    }

    public Backend backend() {
        return backend;
    }

    public String prefix() {
        return prefix;
    }

    /** Names the connections after the server (MySQL processlist, Redis CLIENT LIST); may be empty. */
    public String serverName() {
        return serverName;
    }

    public String jdbcUrl() {
        return jdbcUrl;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String redisHost() {
        return redisHost;
    }

    public int redisPort() {
        return redisPort;
    }

    /** Empty without Redis ACL users (the {@code default} user). */
    public String redisUser() {
        return redisUser;
    }

    public String redisPassword() {
        return redisPassword;
    }

    public int redisDatabase() {
        return redisDatabase;
    }

    public boolean redisSsl() {
        return redisSsl;
    }

    /** Where it is, without the password: for logs. */
    public String describe() {
        if (backend == Backend.REDIS) {
            return "Redis " + (redisUser.isEmpty() ? "" : redisUser + "@") + redisHost + ":" + redisPort + "/"
                    + redisDatabase + (redisSsl ? " (TLS)" : "") + ", keys " + prefix + "*";
        }
        String where = jdbcUrl.replaceFirst("^jdbc:[a-z]+://", "");
        int options = where.indexOf('?');
        return "MySQL " + username + "@" + (options < 0 ? where : where.substring(0, options)) + ", tables "
                + prefix + "arenas and " + prefix + "joins";
    }

    public static final class Builder {

        private final Backend backend;
        private final String prefix;
        private String serverName = "";
        private String jdbcUrl;
        private String username = "";
        private String password = "";
        private String redisHost;
        private int redisPort;
        private String redisUser = "";
        private String redisPassword = "";
        private int redisDatabase;
        private boolean redisSsl;

        private Builder(Backend backend, String prefix) {
            if (prefix == null || !prefix.matches(PREFIX_CHARACTERS)) {
                throw new IllegalArgumentException("prefix may only have letters, digits and _ : -: " + prefix);
            }
            this.backend = backend;
            this.prefix = prefix;
        }

        public Builder serverName(String serverName) {
            this.serverName = serverName == null ? "" : serverName;
            return this;
        }

        /** Redis ACL user and password; an empty user is Redis' {@code default} user. */
        public Builder redisAuth(String user, String password) {
            this.redisUser = user == null ? "" : user.trim();
            this.redisPassword = password == null ? "" : password;
            return this;
        }

        public Builder redisDatabase(int database) {
            if (database < 0) {
                throw new IllegalArgumentException("database must be 0 or more: " + database);
            }
            this.redisDatabase = database;
            return this;
        }

        public Builder redisSsl(boolean ssl) {
            this.redisSsl = ssl;
            return this;
        }

        public ClusterSettings build() {
            if (backend == Backend.MYSQL && prefix.matches(".*[:-].*")) {
                throw new IllegalArgumentException("a table prefix may only have letters, digits and _: " + prefix);
            }
            return new ClusterSettings(this);
        }
    }
}
