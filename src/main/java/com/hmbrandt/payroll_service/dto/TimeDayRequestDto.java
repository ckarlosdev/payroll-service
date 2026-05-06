package com.hmbrandt.payroll_service.dto;

import java.time.LocalDate;
import java.util.List;

public record TimeDayRequestDto(
        Long id,
        Long timesheetId,
        LocalDate timeDay,
        List<DayEntryResponseDto> entries
) {
}
