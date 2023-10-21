package com.example.demo.util;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Constants {

    // A predefined list of holidays. Adjust this as per your requirement.
    public static final List<LocalDate> HOLIDAYS = Arrays.asList(
            // New Year's Day from 2000 to 2022
            LocalDate.of(2000, 1, 1),
            LocalDate.of(2001, 1, 1),
            LocalDate.of(2002, 1, 1),
            // ... continue for every year ...
            LocalDate.of(2022, 1, 1),

            // Independence Day from 2000 to 2022
            LocalDate.of(2000, 7, 4),
            LocalDate.of(2001, 7, 4),
            LocalDate.of(2002, 7, 4),
            // ... continue for every year ...
            LocalDate.of(2022, 7, 4),

            // Christmas Day from 2000 to 2022
            LocalDate.of(2000, 12, 25),
            LocalDate.of(2001, 12, 25),
            LocalDate.of(2002, 12, 25),
            // ... continue for every year ...
            LocalDate.of(2022, 12, 25)

            // TODO: Add other holidays for each year as required or even better is to fetch them from an official public API
    );


}
