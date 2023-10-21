package com.example.csv.service;

import com.example.csv.model.Employee;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface EmployeeOverlappingService {

    Pair<Pair<Integer, Integer>, Long> findLongestWorkingPair(List<Employee> employees);
}
