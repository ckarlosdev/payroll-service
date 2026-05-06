package com.hmbrandt.payroll_service.dto;

import java.time.LocalDate;
import java.util.List;

public record TimesheetRequestDto(
        Long id,
        Long employeesId,
        String user,
        Integer timeYear,
        Integer timeWeek,
        LocalDate periodStartDate,
        String notes,
        String flow,
        List<TimeDayRequestDto> days
) {}

