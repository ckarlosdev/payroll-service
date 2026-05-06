package com.hmbrandt.payroll_service.dto;

import com.hmbrandt.payroll_service.entity.TimeDay;

import java.time.LocalDate;
import java.util.List;

public record TimesheetResponseDto(
        Long id,
        Long employeesId,
        Integer timeYear,
        Integer timeWeek,
        LocalDate periodStartDate,
        String notes,
        String flow
) {}
