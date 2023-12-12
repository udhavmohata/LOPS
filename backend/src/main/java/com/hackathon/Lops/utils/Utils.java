package com.hackathon.Lops.utils;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    @SneakyThrows
    public static Date getParsedDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.parse(dateStr);
    }

    public static LocalDateTime stringToLocalDateTime(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(date, formatter);

        // Convert LocalDate to LocalDateTime by adding the time component (midnight in this case)
       return localDate.atStartOfDay();
    }
    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return localDateTime.format(formatter);
    }

    public static long calculateMinutesDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        // Calculate the duration between two LocalDateTime objects
        Duration duration = Duration.between(dateTime1, dateTime2);

        // Get the total number of minutes in the duration
        return duration.toMinutes();
    }

}
