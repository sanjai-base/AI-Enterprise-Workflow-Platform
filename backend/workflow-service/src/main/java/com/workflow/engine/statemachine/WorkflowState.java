package com.workflow.engine.statemachine;

public enum WorkflowState {
    DRAFT,
    READY,
    IN_PROGRESS,
    PENDING_APPROVAL,
    APPROVED,
    REJECTED,
    COMPLETED,
    FAILED
}
