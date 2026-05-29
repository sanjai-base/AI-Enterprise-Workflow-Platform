package com.workflow.engine.service;

import com.workflow.engine.domain.StepStatus;
import com.workflow.engine.domain.WorkflowInstance;
import com.workflow.engine.repository.WorkflowInstanceRepository;
import com.workflow.engine.statemachine.WorkflowEvent;
import com.workflow.engine.statemachine.WorkflowState;
import com.workflow.engine.statemachine.WorkflowStateInterceptor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WorkflowExecutionService {

    private final WorkflowInstanceRepository repository;
    private final StateMachineFactory<WorkflowState, WorkflowEvent> factory;
    private final WorkflowStateInterceptor stateInterceptor;

    public WorkflowExecutionService(WorkflowInstanceRepository repository, 
                                    StateMachineFactory<WorkflowState, WorkflowEvent> factory,
                                    WorkflowStateInterceptor stateInterceptor) {
        this.repository = repository;
        this.factory = factory;
        this.stateInterceptor = stateInterceptor;
    }

    @Transactional
    public WorkflowInstance startWorkflow(UUID workflowId, UUID userId) {
        WorkflowInstance instance = new WorkflowInstance();
        instance.setWorkflowId(workflowId);
        instance.setInitiatedBy(userId);
        instance.setStatus(StepStatus.PENDING);
        instance.setCurrentStep("START");
        instance.setSlaDueAt(LocalDateTime.now().plusDays(1)); // Default SLA
        
        instance = repository.save(instance);

        StateMachine<WorkflowState, WorkflowEvent> sm = build(instance);
        sendEvent(instance.getId(), sm, WorkflowEvent.SUBMIT);
        sendEvent(instance.getId(), sm, WorkflowEvent.START_EXECUTION);

        return repository.findById(instance.getId()).orElse(instance);
    }

    @Transactional
    public WorkflowInstance approveStep(UUID instanceId) {
        WorkflowInstance instance = repository.findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Instance not found"));
        
        StateMachine<WorkflowState, WorkflowEvent> sm = build(instance);
        sendEvent(instanceId, sm, WorkflowEvent.APPROVE);
        return repository.findById(instanceId).orElse(instance);
    }
    
    @Transactional
    public WorkflowInstance rejectStep(UUID instanceId) {
        WorkflowInstance instance = repository.findById(instanceId)
                .orElseThrow(() -> new RuntimeException("Instance not found"));
        
        StateMachine<WorkflowState, WorkflowEvent> sm = build(instance);
        sendEvent(instanceId, sm, WorkflowEvent.REJECT);
        return repository.findById(instanceId).orElse(instance);
    }

    private void sendEvent(UUID instanceId, StateMachine<WorkflowState, WorkflowEvent> sm, WorkflowEvent event) {
        Message<WorkflowEvent> msg = MessageBuilder.withPayload(event)
                .setHeader("workflow_instance_id", instanceId)
                .build();
        sm.sendEvent(msg);
    }

    private StateMachine<WorkflowState, WorkflowEvent> build(WorkflowInstance instance) {
        StateMachine<WorkflowState, WorkflowEvent> sm = factory.getStateMachine(instance.getId().toString());
        sm.stop();
        
        // Restore state machine from DB status
        WorkflowState currentState = mapStatusToState(instance.getStatus());
        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.addStateMachineInterceptor(stateInterceptor);
                    sma.resetStateMachine(new DefaultStateMachineContext<>(currentState, null, null, null));
                });
        sm.start();
        return sm;
    }
    
    private WorkflowState mapStatusToState(StepStatus status) {
        switch (status) {
            case IN_PROGRESS: return WorkflowState.IN_PROGRESS;
            case PENDING: return WorkflowState.PENDING_APPROVAL;
            case APPROVED: return WorkflowState.APPROVED;
            case REJECTED: return WorkflowState.REJECTED;
            case COMPLETED: return WorkflowState.COMPLETED;
            case FAILED: return WorkflowState.FAILED;
            default: return WorkflowState.DRAFT;
        }
    }
}
