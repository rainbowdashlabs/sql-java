package de.chojo.mariadbbasic;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerformanceTest {
    private static int benchmarkCalls = 50000;
    private static int benchmarkStringLength = 5000;

    @Test
    public void parallelTest() {
        MysqlDataSource mysqlDataSource = new MysqlConnectionPoolDataSource();
        mysqlDataSource.setServerName("localhost");
        mysqlDataSource.setPassword("zdGNSsdsQ1j22GJZuRgZ");
        mysqlDataSource.setPortNumber(3306);
        mysqlDataSource.setDatabaseName("test");
        mysqlDataSource.setUser("root");
        try {
            if (!mysqlDataSource.getConnection().isValid(1000)) {
                System.out.println("Not valid");
                return;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return;
        }
        parallelRequests(mysqlDataSource);

    }

    @Test
    public void parallelRequests(DataSource source) {
        AtomicInteger connections = new AtomicInteger();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        prepareDatabase(source);

        List<String> randomStrings = getRandomStrings(benchmarkCalls, benchmarkStringLength);

        long duration = System.currentTimeMillis();

        for (int i = 0; i < benchmarkCalls; i++) {
            int id = i;
            executor.execute(() -> {
                connections.incrementAndGet();
                try (Connection conn = source.getConnection(); PreparedStatement stmt =
                        conn.prepareStatement("Insert into some_table(id, message) VALUES(?, ?)");
                     PreparedStatement stmt2 = conn.prepareStatement("")) {
                    conn.setAutoCommit(false);
                    stmt.setInt(1, id);
                    stmt.setString(2, randomStrings.get(id));
                    stmt.execute();
                    conn.commit();
                } catch (SQLException e) {
                    System.out.println("Could not insert data.");
                    e.printStackTrace();
                    System.out.println("Connections: " + connections.get());
                }
                connections.decrementAndGet();
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1L, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            System.out.println("Timout");
            e.printStackTrace();
        }

        duration = System.currentTimeMillis() - duration;

        System.out.println(String.format("%d Parallel Requests took %d ms | %d seconds", benchmarkCalls, duration, duration / 1000));

        clearDatabase(source);
    }

    public static void prepareDatabase(DataSource source) {
        clearDatabase(source);

        try (Connection conn = source.getConnection(); PreparedStatement stmt
                = conn.prepareStatement("CREATE TABLE IF NOT EXISTS some_table(id int not null , message text not null)")) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Could not prepare database.");
            e.printStackTrace();
        }
    }

    public static void clearDatabase(DataSource source) {
        try (Connection conn = source.getConnection(); PreparedStatement stmt
                = conn.prepareStatement("drop table if exists some_table")) {
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Could not clear database.");
            e.printStackTrace();
        }
    }

    // Proudly stolen from https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
    public static class RandomString {

        /**
         * Generate a random string.
         */
        public String nextString() {
            for (int idx = 0; idx < buf.length; ++idx) {
                buf[idx] = symbols[random.nextInt(symbols.length)];
            }
            return new String(buf);
        }

        public static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        public static final String LOWER = UPPER.toLowerCase(Locale.ROOT);

        public static final String DIGITS = "0123456789";

        public static final String ALPHANUM = UPPER + LOWER + DIGITS;

        private final Random random;

        private final char[] symbols;

        private final char[] buf;

        public RandomString(int length, Random random, String symbols) {
            if (length < 1) throw new IllegalArgumentException();
            if (symbols.length() < 2) throw new IllegalArgumentException();
            this.random = Objects.requireNonNull(random);
            this.symbols = symbols.toCharArray();
            this.buf = new char[length];
        }

        /**
         * Create an alphanumeric string generator.
         */
        public RandomString(int length, Random random) {
            this(length, random, ALPHANUM);
        }

        /**
         * Create an alphanumeric strings from a secure generator.
         */
        public RandomString(int length) {
            this(length, new SecureRandom());
        }

        /**
         * Create session identifiers.
         */
        public RandomString() {
            this(21);
        }
    }

    private List<String> getRandomStrings(int amount, int length) {
        RandomString randomString = new RandomString(length, ThreadLocalRandom.current());
        return IntStream.range(0, amount).mapToObj(i -> randomString.nextString()).collect(Collectors.toList());
    }
}
