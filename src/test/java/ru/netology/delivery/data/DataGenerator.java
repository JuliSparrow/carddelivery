package ru.netology.delivery.data;

import com.github.javafaker.Faker;
import lombok.Value;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DataGenerator {
    private static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy";
    private static final String[] CITIES_RU = {"Ставрополь", "Тамбов", "Хабаровск", "Ярославль" , "Петропавловск-Камчатский" , "Петрозаводск" , "Иваново" , "Владикавказ" , "Владивосток" , "Благовещенск"};
    private static final String[] CITIES_EN = {"Stavropol", "Tambov", "Habarovsk", "Yaroslavl" , "Petropavlovsk-Kamchatskii" , "Petrozavodsk" , "Ivanovo" , "Vladikavkaz" , "Vladivostok" , "Blagovechensk"};
    private static final Map<String, String[]> cities = new HashMap<>();

    static {
        cities.put("ru", CITIES_RU);
        cities.put("en", CITIES_EN);
    }

    private DataGenerator() {
    }

    public static String generateDate(int shift) {
        return generateDate(shift, DEFAULT_DATE_PATTERN);
    }

    private static String generateDate(int shift, String pattern) {
        return DateFormatUtils.format(Timestamp.valueOf(LocalDateTime.now().plusDays(shift)), pattern);
    }

    public static String generateCity(String locale) {
        int i = new Random().nextInt(cities.size());
        return cities.get(locale.toLowerCase())[i];
    }

    public static String generateName(String locale) {
        return Faker.instance(LocaleUtils.toLocale(locale)).name().name();
    }

    public static String generatePhone(String locale) {
        return Faker.instance(LocaleUtils.toLocale(locale)).phoneNumber().cellPhone();
    }

    public static class Registration {
        private Registration() {
        }

        public static UserInfo generateUser(String locale) {
            return new UserInfo(
                    generateCity(locale),
                    generateName(locale),
                    generatePhone(locale)
            );
        }
    }

    @Value
    public static class UserInfo {
        String city;
        String name;
        String phone;
    }
}
