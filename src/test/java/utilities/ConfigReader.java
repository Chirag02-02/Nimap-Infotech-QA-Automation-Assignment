package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Utility class to read application configuration properties.
 * Prioritizes Environment Variables over config.properties for security.
 */
public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String configPath = "src/test/resources/config.properties";
        try (InputStream input = new FileInputStream(configPath)) {
            properties.load(input);
        } catch (IOException e) {
            // Fallback: try loading from classpath
            try (InputStream cpInput = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (cpInput != null) {
                    properties.load(cpInput);
                }
            } catch (IOException ignored) {
            }
        }
    }

    public static String getProperty(String key) {
        // First check environment variable (converting dots to underscores and uppercase)
        String envKey = key.replace('.', '_').toUpperCase();
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        // Also check direct key in environment variables
        envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }

        // Also check JVM system property (-Dkey=value)
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }

        // Fall back to config.properties
        return properties.getProperty(key, "").trim();
    }

    public static String getAppUrl() {
        String url = getProperty("app.url");
        return url.isEmpty() ? "https://test.fieldforceconnect.com/" : url;
    }

    public static String getBrowser() {
        String browser = getProperty("browser");
        return browser.isEmpty() ? "chrome" : browser;
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("headless"));
    }

    public static int getExplicitWait() {
        try {
            return Integer.parseInt(getProperty("explicit.wait"));
        } catch (NumberFormatException e) {
            return 15;
        }
    }

    public static int getImplicitWait() {
        try {
            return Integer.parseInt(getProperty("implicit.wait"));
        } catch (NumberFormatException e) {
            return 10;
        }
    }

    public static String getValidUsername() {
        return getProperty("valid.username");
    }

    public static String getValidPassword() {
        return getProperty("valid.password");
    }
}

