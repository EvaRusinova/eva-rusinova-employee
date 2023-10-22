package com.example.csv;

import com.example.csv.model.Employee;
import com.example.csv.service.CsvReader;
import com.example.csv.service.EmployeeOverlappingService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication implements ApplicationRunner {

    private final CsvReader csvReader;
    private final EmployeeOverlappingService employeeOverlappingService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // TODO: this method could be more generic and refactored to be called
        //  the way that we provide fileName and Clazz type as follows:
        //  csvReader.readCsvFile("employee.csv", Employee.class);

        List<Employee> employees = csvReader.readCsvFileByName("static/" + "employee.csv");

        System.out.println("Parsed Employees:");
        System.out.println(employees.stream().map(Object::toString).collect(Collectors.joining("\n")));
        System.out.println("\n-------------------------------\n");

        System.out.println("Total parsed employees: " + employees.size());
        Pair<Pair<Integer, Integer>, Long> result = employeeOverlappingService.findLongestWorkingPair(employees);

        if (result != null) {
            System.out.println("Pair of Employees who have worked together on common projects for the longest period of time:");
            System.out.println(result.getLeft().getLeft() + " and " + result.getLeft().getRight() + " for " + result.getRight() + " days.");
        } else {
            System.out.println("No employee pairs found with overlapping project durations.");
        }

    }

}
