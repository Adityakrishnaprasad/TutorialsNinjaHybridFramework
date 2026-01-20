package utilities;

import io.github.cdimascio.dotenv.Dotenv;

public class configurationReader {
   private static Dotenv dotenv;

    static {
        dotenv = Dotenv.configure()
                .directory(System.getProperty("user.dir"))
                .filename(".env")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        System.out.println("Loaded environment variables from .env");
    }

   public static String get(String key) {

    // 1️⃣ Maven / JVM property (HIGHEST PRIORITY)
    String value = System.getProperty(key);
    if (value != null && !value.isBlank()) {
        return value.trim();
    }

    // 2️⃣ OS environment variable
    value = System.getenv(key);
    if (value != null && !value.isBlank()) {
        return value.trim();
    }

    // 3️⃣ .env file
    value = dotenv.get(key);
    if (value != null && !value.isBlank()) {
        return value.trim();
    }

    return null;
}


}
