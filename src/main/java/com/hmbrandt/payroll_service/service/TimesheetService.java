package com.hmbrandt.payroll_service.service;

import com.hmbrandt.payroll_service.dto.TimesheetResponseDto;
import com.hmbrandt.payroll_service.dto.TimesheetRequestDto;

import java.util.List;

public interface TimesheetService {
    TimesheetResponseDto save(TimesheetRequestDto timesheetDto);

    TimesheetResponseDto findById(Long id);

    List<TimesheetResponseDto> findAll();
}
