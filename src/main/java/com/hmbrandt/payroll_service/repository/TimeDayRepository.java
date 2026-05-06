package com.hmbrandt.payroll_service.repository;

import com.hmbrandt.payroll_service.entity.TimeDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeDayRepository extends JpaRepository<TimeDay, Long> {
    List<TimeDay> findAllByTimesheetIdAndDeletedAtIsNull(Long timesheetId);
}
