package com.example.csv.service;

import com.example.csv.dto.EmployeeGridResponse;
import com.example.csv.model.Employee;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public interface EmployeeOverlappingService {
    Pair<Pair<Integer, Integer>, Long> findLongestWorkingPair(List<Employee> employees);

    Integer findProjectWithLongestDurationForPair(Pair<Pair<Integer, Integer>, Long> pair, List<Employee> employees);

    List<EmployeeGridResponse> getCommonProjectsForPair(Pair<Pair<Integer, Integer>, Long> pair, List<Employee> employees);
}
