package utilities;

import java.io.File;

import io.github.cdimascio.dotenv.Dotenv;

public class configurationReader {
    private static Dotenv dotenv;


	static {
        File envFile = new File(".env");

        if (envFile.exists()) {
            dotenv = Dotenv.configure().filename(".env").load();
            System.out.println(" Loaded environment variables from .env");
        } else {
            dotenv = Dotenv.configure().filename(".env.example").load();
            System.out.println(" .env not found, using .env.example instead");
        }
    }

	public static String get(String key) {
		return dotenv.get(key);
	}
}
