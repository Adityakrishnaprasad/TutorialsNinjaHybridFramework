package utilities;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoggerLoad {
    public static final Logger log = LogManager.getLogger(LoggerLoad.class);

    // Log lines of the test running on this thread (each browser runs on its own thread),
    // attached to that test in the Allure report
    private static final ThreadLocal<StringBuilder> testLog = ThreadLocal.withInitial(StringBuilder::new);
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void startTestLog() {
        testLog.get().setLength(0);
    }

    public static String getTestLog() {
        return testLog.get().toString();
    }

    private static void capture(String level, String message) {
        testLog.get().append('[').append(LocalTime.now().format(TIME)).append("] ")
                .append(level).append(" - ").append(message).append(System.lineSeparator());
    }

    public static void info(String message) {
        log.info(message);
        capture("INFO ", message);
    }

    public static void error(String message) {
        log.error(message);
        capture("ERROR", message);
    }

    public static void warn(String message) {
        log.warn(message);
        capture("WARN ", message);
    }

    public static void debug(String message) {
        log.debug(message);
    }
}
