package server.storage;

public final class DatabaseConfig {

    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static DatabaseConfig fromArgs(String[] args) {
        String url = envOrDefault("LAB7_DB_URL", "jdbc:postgresql://pg:5432/studs");
        String user = envOrDefault("LAB7_DB_USER", System.getProperty("user.name"));
        String password = envOrDefault("LAB7_DB_PASSWORD", "");
        if (args.length > 1) url = args[1];
        if (args.length > 2) user = args[2];
        if (args.length > 3) password = args[3];
        return new DatabaseConfig(url, user, password);
    }

    private static String envOrDefault(String name, String fallback) {
        String val = System.getenv(name);
        return val != null ? val : fallback;
    }

    public String getUrl() { return url; }
    public String getUser() { return user; }
    public String getPassword() { return password; }
}
