package com.keyingym.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class AppLogger {

    private static final Logger LOGGER =
            Logger.getLogger("GymManagementSystem");

    private static boolean configured = false;

    private AppLogger() {
        // Prevent instantiation.
    }

    public static synchronized void configure() {
        if (configured) {
            return;
        }

        try {
            FileHandler fileHandler =
                    new FileHandler("app.log", true);

            fileHandler.setFormatter(new SimpleFormatter());

            LOGGER.setUseParentHandlers(false);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.INFO);

            configured = true;

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Unable to configure application logging.",
                    e
            );
        }
    }

    public static void info(String message) {
        configure();
        LOGGER.info(message);
    }

    public static void warning(String message) {
        configure();
        LOGGER.warning(message);
    }

    public static void error(String message, Exception exception) {
        configure();
        LOGGER.log(Level.SEVERE, message, exception);
    }
}
