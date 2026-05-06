package com.hmbrandt.payroll_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "time_days")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_days_id")
    private Long id;

//    @Column(name = "timesheets_id", nullable = false)
//    private Long timesheetsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timesheets_id", nullable = false)
    private Timesheet timesheet;

    @Column(name = "time_day", nullable = false)
    private LocalDate timeDay;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "timeDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DayEntry> entries;
}
