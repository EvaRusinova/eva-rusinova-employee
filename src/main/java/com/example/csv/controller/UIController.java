package com.example.csv.controller;

import com.example.csv.dto.CustomResponse;
import com.example.csv.dto.EmployeeGridResponse;
import com.example.csv.model.Employee;
import com.example.csv.service.CsvReader;
import com.example.csv.service.EmployeeOverlappingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@Tag(name = "Csv")
@RequestMapping("/")
@RequiredArgsConstructor
public class UIController {

    private final CsvReader csvReader;
    private final EmployeeOverlappingService employeeOverlappingService;

    @GetMapping
    public String showCsvPage(Model model) {
        model.addAttribute("result", null); // Initialize the result model
        return "csv";
    }

    @PostMapping("/handleCsvFile")
    public ResponseEntity<CustomResponse> handleCsvFile(@RequestParam("file") MultipartFile file) throws IOException {
        List<Employee> employees = csvReader.readCsvFile(file);
        Pair<Pair<Integer, Integer>, Long> longestWorkingPair = employeeOverlappingService.findLongestWorkingPair(employees);
        Integer projectWithLongestDurationForPair = employeeOverlappingService.findProjectWithLongestDurationForPair(longestWorkingPair, employees);
        List<EmployeeGridResponse> employeeGridResponseList = employeeOverlappingService.getCommonProjectsForPair(longestWorkingPair, employees);

        // Create a custom response object to include both results
        CustomResponse response = new CustomResponse(longestWorkingPair.toString(), projectWithLongestDurationForPair, employeeGridResponseList);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
