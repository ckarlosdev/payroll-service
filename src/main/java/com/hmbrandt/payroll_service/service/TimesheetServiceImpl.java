package com.hmbrandt.payroll_service.service;

import com.hmbrandt.payroll_service.dto.DayEntryResponseDto;
import com.hmbrandt.payroll_service.dto.TimeDayRequestDto;
import com.hmbrandt.payroll_service.dto.TimesheetResponseDto;
import com.hmbrandt.payroll_service.dto.TimesheetRequestDto;
import com.hmbrandt.payroll_service.entity.DayEntry;
import com.hmbrandt.payroll_service.entity.TimeDay;
import com.hmbrandt.payroll_service.entity.Timesheet;
import com.hmbrandt.payroll_service.repository.DayEntryRepository;
import com.hmbrandt.payroll_service.repository.TimeDayRepository;
import com.hmbrandt.payroll_service.repository.TimesheetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetServiceImpl implements TimesheetService{

    private final TimesheetRepository timesheetRepository;
    private final TimeDayRepository timeDayRepository;
    private final DayEntryRepository dayEntryRepository;

    @Override
    @Transactional
    public TimesheetResponseDto save(TimesheetRequestDto timesheetDto){

        Timesheet timesheetNew = toTimesheetEntity(timesheetDto);
        Timesheet timesheetSaved = timesheetRepository.save(timesheetNew);

        saveTimeData(timesheetDto, timesheetSaved);

        /// ///////////////////////////////
        // Falta guardar primer approval //
        /// ///////////////////////////////

        return timesheetResponseDto(timesheetSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public TimesheetResponseDto findById(Long id){
        Timesheet timesheet = timesheetRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));

        return timesheetResponseDto(timesheet);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimesheetResponseDto> findAll(){
        return timesheetRepository.findAll().stream()
                .map(this::timesheetResponseDto)
                .toList();
    }

    private TimesheetResponseDto timesheetResponseDto(Timesheet timesheet){
        return new TimesheetResponseDto(
                timesheet.getId(),
                timesheet.getEmployeesId(),
                timesheet.getTimeYear(),
                timesheet.getTimeWeek(),
                timesheet.getPeriodStartDate(),
                timesheet.getNotes(),
                timesheet.getFlow()
        );
    }

    private Timesheet toTimesheetEntity(TimesheetRequestDto timesheetDto){
        LocalDateTime now = LocalDateTime.now();

        return Timesheet.builder()
                .employeesId(timesheetDto.employeesId())
                .timeYear(timesheetDto.timeYear())
                .timeWeek(timesheetDto.timeWeek())
                .periodStartDate(timesheetDto.periodStartDate())
                .notes(timesheetDto.notes())
                .flow(timesheetDto.flow())
                .createdBy(timesheetDto.user())
                .createdAt(now)
                .updatedBy(timesheetDto.user())
                .updatedAt(now)
                .build();
    }

    private void saveTimeData(TimesheetRequestDto timesheetDto, Timesheet timesheetSaved){
        if(!timesheetDto.days().isEmpty()) return;

        Long timesheetId = timesheetSaved.getId();
        LocalDateTime now = LocalDateTime.now();
        String user = timesheetDto.user();

        List<TimeDay> actualDays = timeDayRepository.findAllByTimesheetIdAndDeletedAtIsNull(timesheetId);
        Map<Long, TimeDay> daysMap = actualDays.stream()
                .collect(Collectors.toMap(TimeDay::getId, Function.identity()));
        List<TimeDay> daysToSave = new ArrayList<>();

        for (TimeDayRequestDto dto : timesheetDto.days()){
            Long dtoId = dto.id();
            TimeDay dayInstance;

            if( dtoId != null && daysMap.containsKey(dtoId) ){
                // update
                dayInstance = daysMap.get(dtoId);

                dayInstance.setTimesheet(timesheetSaved);
                dayInstance.setTimeDay(dto.timeDay());
                dayInstance.setUpdatedBy(user);
                dayInstance.setUpdatedAt(now);

                daysMap.remove(dtoId);
            }else{
                // new
                dayInstance = TimeDay.builder()
                        .timesheet(timesheetSaved)
                        .timeDay(dto.timeDay())
                        .createdBy(user)
                        .createdAt(now)
                        .updatedBy(user)
                        .updatedAt(now)
                        .build();
            }

            TimeDay daySaved = timeDayRepository.save(dayInstance);
            saveHoursData(dto.entries(), daySaved, user, now);
            daysToSave.add(daySaved);
        }

//        timeDayRepository.saveAll(daysToSave);
        List<TimeDay> daysToDelete = new ArrayList<>(daysMap.values());

        if(!daysToDelete.isEmpty()){
            daysToDelete.forEach(day -> {
                day.setUpdatedBy(user);
                day.setUpdatedAt(now);
                day.setDeletedAt(now);
            });
            timeDayRepository.saveAll(daysToDelete);
        }
    }

    private void saveHoursData(
            List<DayEntryResponseDto> hoursDto,
            TimeDay daySaved,
            String user,
            LocalDateTime now
    ) {
        if (hoursDto == null) return;

        List<DayEntry> actualHours = dayEntryRepository.findAllByTimeDayIdAndDeletedAtIsNull(daySaved.getId());
        Map<Long, DayEntry> hoursMap = actualHours.stream()
                .collect(Collectors.toMap(DayEntry::getId, Function.identity()));
        List<DayEntry> hoursToSave = new ArrayList<>();

        for (DayEntryResponseDto hDto : hoursDto) {
            if (hDto.id() != null && hoursMap.containsKey(hDto.id())) {
                // UPDATE HOUR
                DayEntry hourUpdate = hoursMap.get(hDto.id());

                hourUpdate.setTimeDay(daySaved);
                hourUpdate.setJobsId(hDto.jobsId());
                hourUpdate.setStartHour(hDto.startHour());
                hourUpdate.setEndHour(hDto.endHour());
                hourUpdate.setNotes(hDto.notes());
                hourUpdate.setLunch(hDto.lunch());
                hourUpdate.setUpdatedBy(user);
                hourUpdate.setUpdatedAt(now);

                hoursToSave.add(hourUpdate);
                hoursMap.remove(hDto.id());
            } else {
                // NEW HOUR
                DayEntry newHour = DayEntry.builder()
                        .timeDay(daySaved) // Relación FK
                        .jobsId(hDto.jobsId())
                        .startHour(hDto.startHour())
                        .endHour(hDto.endHour())
                        .lunch(hDto.lunch())
                        .notes(hDto.notes())
                        .createdBy(user)
                        .createdAt(now)
                        .updatedBy(user)
                        .updatedAt(now)
                        .build();
                hoursToSave.add(newHour);
            }
        }

        dayEntryRepository.saveAll(hoursToSave);

        hoursMap.values().forEach(h -> {
            h.setDeletedAt(now);
            h.setUpdatedBy(user);
            h.setUpdatedAt(now);
        });
        dayEntryRepository.saveAll(hoursMap.values());
    }
}
