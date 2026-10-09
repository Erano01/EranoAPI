package me.erano.com.api.cluster.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import me.erano.com.api.cluster.BackendContract;
import me.erano.com.api.cluster.ClusterSettings;
import me.erano.com.api.cluster.QuickJoinTest;

/** Against a real Redis: {@code ERANOAPI_CLUSTER_TEST_REDIS=host:port}; skipped without it. */
class RedisBackendTest {

    private static ClusterSettings settings() {
        String address = System.getenv("ERANOAPI_CLUSTER_TEST_REDIS");
        Assumptions.assumeTrue(address != null && !address.isEmpty(), "ERANOAPI_CLUSTER_TEST_REDIS isn't set");
        String[] parts = address.split(":");
        // A key prefix of its own, so the test leaves a real network's keys alone.
        return ClusterSettings.redis(parts[0], Integer.parseInt(parts[1]), "hgtest" + System.nanoTime() + ":").build();
    }

    @Test
    void keepsTheContractAndTellsAboutChanges() throws Exception {
        RedisBackend backend = RedisBackend.connect(settings(), Logger.getLogger("test"));
        try {
            BlockingQueue<String> changed = new LinkedBlockingQueue<>();
            assertTrue(backend.listen(changed::add));
            // The subscription starts on its own thread; give it a moment before publishing.
            Thread.sleep(500);
            BackendContract.check(backend);
            backend.publish("hg-9", Collections.singletonList(QuickJoinTest.arena("hg-9", "x", 0, 1, true)));
            String server = null;
            while (!"hg-9".equals(server)) {
                server = changed.poll(5, TimeUnit.SECONDS);
                if (server == null) {
                    break;
                }
            }
            assertEquals("hg-9", server);
            backend.remove("hg-9");
        } finally {
            backend.close();
        }
    }
}
