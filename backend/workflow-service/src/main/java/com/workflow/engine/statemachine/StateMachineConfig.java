package com.workflow.engine.statemachine;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<WorkflowState, WorkflowEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<WorkflowState, WorkflowEvent> states) throws Exception {
        states
            .withStates()
            .initial(WorkflowState.DRAFT)
            .states(EnumSet.allOf(WorkflowState.class))
            .end(WorkflowState.COMPLETED)
            .end(WorkflowState.REJECTED)
            .end(WorkflowState.FAILED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<WorkflowState, WorkflowEvent> transitions) throws Exception {
        transitions
            .withExternal().source(WorkflowState.DRAFT).target(WorkflowState.READY).event(WorkflowEvent.SUBMIT)
            .and()
            .withExternal().source(WorkflowState.READY).target(WorkflowState.IN_PROGRESS).event(WorkflowEvent.START_EXECUTION)
            .and()
            .withExternal().source(WorkflowState.IN_PROGRESS).target(WorkflowState.PENDING_APPROVAL).event(WorkflowEvent.REQUIRE_APPROVAL)
            .and()
            .withExternal().source(WorkflowState.PENDING_APPROVAL).target(WorkflowState.APPROVED).event(WorkflowEvent.APPROVE)
            .and()
            .withExternal().source(WorkflowState.PENDING_APPROVAL).target(WorkflowState.REJECTED).event(WorkflowEvent.REJECT)
            .and()
            .withExternal().source(WorkflowState.APPROVED).target(WorkflowState.IN_PROGRESS).event(WorkflowEvent.START_EXECUTION)
            .and()
            .withExternal().source(WorkflowState.IN_PROGRESS).target(WorkflowState.COMPLETED).event(WorkflowEvent.COMPLETE)
            .and()
            .withExternal().source(WorkflowState.IN_PROGRESS).target(WorkflowState.FAILED).event(WorkflowEvent.FAIL);
    }
}
