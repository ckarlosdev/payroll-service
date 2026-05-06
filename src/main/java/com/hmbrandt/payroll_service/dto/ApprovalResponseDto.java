package com.hmbrandt.payroll_service.dto;

public record ApprovalResponseDto(
        Long approvalsId,
        String approvalType,
        String createdBy
) {}
