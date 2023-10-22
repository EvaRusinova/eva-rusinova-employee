package com.example.csv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CustomResponse {
    private String longestWorkingPair;
    private Integer projectWithLongestDuration;
    private List<EmployeeGridResponse> employeeGridResponseList;
}
