package snowcode.snowcode.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static LocalDate stringToDate(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
