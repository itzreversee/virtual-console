package me.reversee.vconsole.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateControl {
    public static String getCurrentDate(String format) {

        Instant instant = Instant.now();

        switch (format) {
            case "full" -> {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneId.systemDefault());
                return dtf.format(instant);
            }
            case "hour" -> {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss").withZone(ZoneId.systemDefault());
                return dtf.format(instant);
            }
        }

        System.out.println("Invalid date format (internal error)");
        return null;
    }
}
