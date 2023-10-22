package com.example.csv.service.impl;

import com.example.csv.exception.FutureDateException;
import com.example.csv.model.Employee;
import com.example.csv.service.CsvReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CsvReaderImpl implements CsvReader {

    private final DateParserServiceImpl dateParser;

    public List<Employee> readCsvFileByName(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource(fileName);
        BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));

        List<Employee> employees = new ArrayList<>();

        String line;
        boolean skipHeader = true;

        while ((line = reader.readLine()) != null) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            employees.add(parseEmployeeFromCsvLine(line.trim().split(",\\s*")));
        }
        return employees;
    }

    @Override
    public List<Employee> readCsvFile(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        List<Employee> employees = new ArrayList<>();

        String line;
        boolean skipHeader = true;

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
