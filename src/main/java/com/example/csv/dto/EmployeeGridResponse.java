package com.example.csv.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class EmployeeGridResponse {
    private Integer firstEmployeeId;
    private Integer secondEmployeeId;
    private Integer projectId;
    private Long totalDaysWorked;
}
