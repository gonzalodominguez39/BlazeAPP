package com.emma.Blaze.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserFunction {

    public static LocalDate stringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

}
