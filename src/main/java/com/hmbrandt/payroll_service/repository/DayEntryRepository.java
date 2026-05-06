package com.hmbrandt.payroll_service.repository;

import com.hmbrandt.payroll_service.entity.DayEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayEntryRepository extends JpaRepository<DayEntry, Long> {
    List<DayEntry> findAllByTimeDayIdAndDeletedAtIsNull(Long timeDayId);
}
