package com.hmbrandt.payroll_service.repository;

import com.hmbrandt.payroll_service.entity.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Long> {
}
