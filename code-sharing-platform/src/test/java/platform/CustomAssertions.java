package platform;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.Assert.*;

public class CustomAssertions {

    public static void assertDateFormat(String date) {
        assertTrue("Date format is not valid. Should be yyyy/MM/dd HH:mm:ss. Was [%s]".formatted(date),
                isDateFormatValid(date));
    }

    public static void assertDatesEqual(String expected, String actual) {
        assertDateFormat(actual);
        assertTrue("Dates are not equal\nExpected: [%s]\nBut was:[%s]".formatted(expected, actual),
                areDatesEqual(expected, actual));
    }

    public static void assertJsonEqual(JSONObject expected, JSONObject actual) {
        assertEquals(expected.toString(), actual.toString());
    }

    private static boolean areDatesEqual(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        long time1 = LocalDateTime.parse(date1, formatter).toEpochSecond(ZoneOffset.UTC);
        long time2 = LocalDateTime.parse(date2, formatter).toEpochSecond(ZoneOffset.UTC);
        if (time2 - 3 <= time1 && time1 <= time2 + 3) {
            return true;
        }
        if (time1 - 3 <= time2 && time2 <= time1 + 3) {
            return true;
        }
        return false;
    }

    public static void assertIsUUID(String string) {
        try {
            UUID object = UUID.fromString(string);
        } catch (IllegalArgumentException e) {
            fail("Value of key 'id' is not of UUID type.");
        }
    }

    private static boolean isDateFormatValid(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        try {
            LocalDateTime.parse(date, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
