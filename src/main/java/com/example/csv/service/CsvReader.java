package com.example.csv.service;

import com.example.csv.model.Employee;

import java.io.IOException;
import java.util.List;

public interface CsvReader {

    List<Employee> readCsvFile(String fileName) throws IOException;

}
