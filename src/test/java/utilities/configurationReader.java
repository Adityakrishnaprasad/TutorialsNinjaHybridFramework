package utilities;

import io.github.cdimascio.dotenv.Dotenv;

public class configurationReader {

	private static final Dotenv dotenv = Dotenv.configure().filename("configuration.env") 
			.load();

	public static String get(String key) {
		return dotenv.get(key);
	}
}
