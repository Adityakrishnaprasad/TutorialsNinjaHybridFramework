package utilities;

import java.util.UUID;

import com.github.javafaker.Faker;

public class DataGenerator {

	static Faker faker = new Faker();

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getTelephone() {
        return faker.phoneNumber().subscriberNumber(10);  // 10-digit number
    }

    public static String getEmail() {
        return "user_" + UUID.randomUUID().toString().substring(0, 5) + "@example.com";
    }

    public static String getPassword() {
        return "Pass@" + faker.number().digits(5);
    }
}
