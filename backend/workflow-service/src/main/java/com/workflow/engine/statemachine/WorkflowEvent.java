package com.workflow.engine.statemachine;

public enum WorkflowEvent {
    SUBMIT,
    START_EXECUTION,
    REQUIRE_APPROVAL,
    APPROVE,
    REJECT,
    COMPLETE,
    FAIL
}
