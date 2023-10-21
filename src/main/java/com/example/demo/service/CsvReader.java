package com.example.demo.service;

import com.example.demo.exception.FutureDateException;
import com.example.demo.model.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CsvReader {

    private final DateParserService dateParser;

    public List<Employee> readCsvFile(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));

        List<Employee> employees = new ArrayList<>();

        String line;
        boolean skipHeader = true;  // Assuming your CSV has a header

        while ((line = reader.readLine()) != null) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            employees.add(parseEmployeeFromCsvLine(line.trim().split(",\\s*")));
        }
        return employees;
    }

    private Employee parseEmployeeFromCsvLine(String[] data) {
        Date dateFrom = convertToDate(dateParser.parseDate(data[2]));
        Date dateTo = convertToDate(dateParser.parseDate(data[3]));

        // TODO: It will be nice to have date validator for cases like 2023-88-88

        // Check if the dates are in the future
        if (dateFrom.after(Date.from(Instant.now())) || dateTo.after(Date.from(Instant.now()))) {
            throw new FutureDateException("Date in the future is not allowed");
        }

        return Employee.builder().employeeId(Integer.parseInt(data[0])).projectId(Integer.parseInt(data[1])).dateFrom(convertToDate(dateParser.parseDate(data[2]))).dateTo(convertToDate(dateParser.parseDate(data[3]))).build();
    }

    private Date convertToDate(Date date) {
        if (date == null) {
            return Date.from(Instant.now());
        }
        return Date.from(date.toInstant());
    }

}
