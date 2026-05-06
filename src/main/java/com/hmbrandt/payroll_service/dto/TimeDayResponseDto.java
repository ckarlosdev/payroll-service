package com.hmbrandt.payroll_service.dto;

import com.hmbrandt.payroll_service.entity.DayEntry;
import com.hmbrandt.payroll_service.entity.Timesheet;

import java.time.LocalDate;
import java.util.List;

public record TimeDayResponseDto(
        Long id,
        Long timesheetId,
        LocalDate timeDay,
        List<DayEntryResponseDto> entries
) {}
