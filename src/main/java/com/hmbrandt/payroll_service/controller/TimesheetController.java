package com.hmbrandt.payroll_service.controller;


import com.hmbrandt.payroll_service.dto.TimesheetResponseDto;
import com.hmbrandt.payroll_service.dto.TimesheetRequestDto;
import com.hmbrandt.payroll_service.service.TimesheetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/payroll/timesheet")
@RequiredArgsConstructor
@Tag(name = "Time Sheet", description = "Record the time worked by employees")
public class TimesheetController {

    private final TimesheetService timesheetService;

    @PostMapping
    public ResponseEntity<TimesheetResponseDto> create(
           @RequestBody TimesheetRequestDto timesheetDto
    ){
        TimesheetResponseDto response = timesheetService.save(timesheetDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimesheetResponseDto> getById(@PathVariable Long id) {
        TimesheetResponseDto response = timesheetService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TimesheetResponseDto>> getAll() {
       return ResponseEntity.ok(timesheetService.findAll());
    }

}
