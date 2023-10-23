package com.example.csv.service;

import com.example.csv.model.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CsvReader {
    List<Employee> readCsvFileByName(String fileName) throws IOException;

    List<Employee> readCsvFile(MultipartFile file) throws IOException;
}
