package com.emma.blaze.helpers;

import com.emma.blaze.data.model.Swipe;

import java.time.LocalDate;
import java.time.Period;

public class UserFunctions {


    public static Swipe CrateSwipe(long userId, Long swipedUserId, String direction) {
        Swipe swipe = new Swipe();
        swipe.setUserId(userId);
        swipe.setSwipedUserId(swipedUserId);
        swipe.setDirection(direction);
        return swipe;
    }


    public static String calcularEdad(String fechaNacimientoStr) {
        if (fechaNacimientoStr == null || fechaNacimientoStr.isEmpty()) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser nula o vacía");
        }

        LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();

        return String.valueOf(edad);
    }

}

