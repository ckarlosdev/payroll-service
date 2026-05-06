package com.hmbrandt.payroll_service.dto;

import com.hmbrandt.payroll_service.entity.Approval;
import com.hmbrandt.payroll_service.entity.TimeDay;

import java.time.LocalTime;
import java.util.List;

public record DayEntryResponseDto(
        Long id,
        Long jobsId,
        LocalTime startHour,
        LocalTime endHour,
        String notes,
        Boolean lunch,
        List<ApprovalResponseDto> approvals
){}
