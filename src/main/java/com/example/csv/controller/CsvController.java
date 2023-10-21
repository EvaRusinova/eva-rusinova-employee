package com.example.csv.controller;

import com.example.csv.model.Employee;
import com.example.csv.service.CsvReader;
import com.example.csv.service.EmployeeOverlappingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Tag(name = "Csv")
@RequestMapping("/csv")
@RequiredArgsConstructor
public class CsvController {

    private final CsvReader csvReader;
    private final EmployeeOverlappingService employeeOverlappingService;

    @PostMapping("/handleEmployees")
    public ResponseEntity<?> handleEmployeesFromCsv() throws IOException {
        List<Employee> employees = csvReader.readCsvFile("static/" + "employee.csv");
        Pair<Pair<Integer, Integer>, Long> longestWorkingPair = employeeOverlappingService.findLongestWorkingPair(employees);
        return ResponseEntity.ok(longestWorkingPair);
    }

}
