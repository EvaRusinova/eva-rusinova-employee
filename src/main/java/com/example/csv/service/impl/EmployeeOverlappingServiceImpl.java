package com.example.csv.service.impl;

import com.example.csv.model.Employee;
import com.example.csv.service.EmployeeOverlappingService;
import com.example.csv.util.Constants;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class EmployeeOverlappingServiceImpl implements EmployeeOverlappingService {

    /**
     * Method to find the pair of employees who have worked together on common projects
     * for the longest period of time.
     *
     * @param employees - List of all employees read from the CSV
     * @return - Pair of Employee IDs who have the longest overlapping work duration and the duration in days.
     */
    public Pair<Pair<Integer, Integer>, Long> findLongestWorkingPair(List<Employee> employees) {
        // A map to store the work duration of each pair of employees.
        // Key is the employee pair and Value is the total days they've worked together.
        Map<Pair<Integer, Integer>, Long> pairWorkDuration = new HashMap<>();

        // Iterating over each employee
        for (Employee e1 : employees) {
            // Iterating again over all employees to compare each employee with every other
            for (Employee e2 : employees) {
                // Check if both employees have worked on the same project
                // and to avoid double counting (e1 < e2 ensures that we count (e1, e2) and not (e2, e1) again)
                if (Objects.equals(e1.getProjectId(), e2.getProjectId()) && e1.getEmployeeId() < e2.getEmployeeId()) {

                    // Determine the start date of the overlapping period.
                    // It's the latter of the two employee's start dates.
                    Date overlapStart = e1.getDateFrom().after(e2.getDateFrom()) ? e1.getDateFrom() : e2.getDateFrom();

                    // Determine the end date of the overlapping period.
                    // It's the earlier of the two employee's end dates.
                    Date overlapEnd = e1.getDateTo().before(e2.getDateTo()) ? e1.getDateTo() : e2.getDateTo();

                    // Check if there's a valid overlap (start date is before the end date)
                    if (overlapStart.before(overlapEnd)) {
                        // Calculate the number of days they worked together during this overlap.
                        // We add 1 to the result to include both the start and end days in the calculated duration.
                        long daysWorkedTogether = excludeWeekendsAndHolidays(overlapStart, overlapEnd);

                        // Create a pair of their employee IDs
                        Pair<Integer, Integer> pair = Pair.of(e1.getEmployeeId(), e2.getEmployeeId());

                        // Update the total days this pair has worked together across all projects
                        pairWorkDuration.put(pair, pairWorkDuration.getOrDefault(pair, 0L) + daysWorkedTogether);
                    }
                }
            }
        }

        // Return the pair with the maximum working duration
        return pairWorkDuration.entrySet().stream()
                // Find the entry with the maximum value
                .max(Map.Entry.comparingByValue())
                // Convert the entry to the desired output format
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                // If no entry is found, return null
                .orElse(null);
    }

    /**
     * Calculates the working days between two dates, excluding weekends and official holidays.
     *
     * @param startDate - The start date.
     * @param endDate   - The end date.
     * @return - The number of working days between the two dates.
     */
    private long excludeWeekendsAndHolidays(Date startDate, Date endDate) {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysCount = 0L;

        while (!start.isAfter(end)) {
            if (!isWeekendOrHoliday(start)) {
                daysCount++;
            }
            start = start.plusDays(1);
        }

        return daysCount;
    }

    /**
     * Checks if a date is a weekend or an official holiday.
     *
     * @param date - The date to check.
     * @return - True if the date is a weekend or holiday, false otherwise.
     */
    private boolean isWeekendOrHoliday(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY || Constants.HOLIDAYS.contains(date);
    }
}
