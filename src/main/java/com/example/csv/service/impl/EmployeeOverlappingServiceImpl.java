package com.example.csv.service.impl;

import com.example.csv.dto.EmployeeGridResponse;
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
     * Method to find the project with the longest duration for the pair
     *
     * @return ProjectID
     */
    @Override
    public Integer findProjectWithLongestDurationForPair(Pair<Pair<Integer, Integer>, Long> pair, List<Employee> employees) {
        if (pair != null) {
            Pair<Integer, Integer> employeePair = pair.getLeft();
            Integer employeeId1 = employeePair.getLeft();
            Integer employeeId2 = employeePair.getRight();

            // Create a map to track the total days worked by each project
            Map<Integer, Long> projectDaysWorked = new HashMap<>();

            for (Employee employee : employees) {
                if ((employee.getEmployeeId().equals(employeeId1) || employee.getEmployeeId().equals(employeeId2)) && employee.getProjectId() != null) {

                    int projectId = employee.getProjectId();
                    long daysWorked = excludeWeekendsAndHolidays(employee.getDateFrom(), employee.getDateTo());

                    // Update the total days worked for this project
                    projectDaysWorked.put(projectId, projectDaysWorked.getOrDefault(projectId, 0L) + daysWorked);
                }
            }

            // Find the project with the longest duration
            Optional<Map.Entry<Integer, Long>> longestProject = projectDaysWorked.entrySet().stream().max(Map.Entry.comparingByValue());

            if (longestProject.isPresent()) {
                return longestProject.get().getKey();
            }
        }

        // Return null if no project found
        return null;
    }

    /**
     * Method to find the project all common projects for the pair
     *
     * @return - gridResponse
     */
    @Override
    public List<EmployeeGridResponse> getCommonProjectsForPair(Pair<Pair<Integer, Integer>, Long> pair, List<Employee> employees) {
        // Initialize a list to store the common projects of the pair
        List<EmployeeGridResponse> result = new ArrayList<>();

        if (pair != null) {
            // Extract the employee IDs from the pair
            Pair<Integer, Integer> employeePair = pair.getLeft();
            Integer employeeId1 = employeePair.getLeft();
            Integer employeeId2 = employeePair.getRight();

            // Create a map to track the total days worked by each project
            Map<Integer, Long> projectDaysWorked = new HashMap<>();

            // Iterate over the employees to find the common projects
            Set<Integer> processedProjects = new HashSet<>();

            for (Employee employee : employees) {
                if ((employee.getEmployeeId().equals(employeeId1) || employee.getEmployeeId().equals(employeeId2)) && !processedProjects.contains(employee.getProjectId())) {

                    int projectId = employee.getProjectId();

                    // Calculate the days worked for this project by the pair
                    long daysWorked = excludeWeekendsAndHolidays(employee.getDateFrom(), employee.getDateTo());

                    // Update the total days worked for this project
                    projectDaysWorked.put(projectId, daysWorked);

                    // Mark this project as processed to avoid double counting
                    processedProjects.add(projectId);
                }
            }

            // Create EmployeeGridResponse objects for each project
            for (Map.Entry<Integer, Long> entry : projectDaysWorked.entrySet()) {
                Integer projectId = entry.getKey();
                Long totalDaysWorked = entry.getValue();

                EmployeeGridResponse gridResponse = new EmployeeGridResponse();
                gridResponse.setFirstEmployeeId(employeeId1);
                gridResponse.setSecondEmployeeId(employeeId2);
                gridResponse.setProjectId(projectId);
                gridResponse.setTotalDaysWorked(totalDaysWorked);

                result.add(gridResponse);
            }
        }

        return result;
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
