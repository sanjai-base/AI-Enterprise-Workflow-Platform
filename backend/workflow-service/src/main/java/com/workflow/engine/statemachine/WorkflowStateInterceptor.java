package com.workflow.engine.statemachine;

import com.workflow.engine.domain.StepStatus;
import com.workflow.engine.domain.WorkflowInstance;
import com.workflow.engine.repository.WorkflowInstanceRepository;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class WorkflowStateInterceptor extends StateMachineInterceptorAdapter<WorkflowState, WorkflowEvent> {

    private final WorkflowInstanceRepository repository;

    public WorkflowStateInterceptor(WorkflowInstanceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void preStateChange(State<WorkflowState, WorkflowEvent> state, 
                             Message<WorkflowEvent> message, 
                             Transition<WorkflowState, WorkflowEvent> transition, 
                             StateMachine<WorkflowState, WorkflowEvent> stateMachine, 
                             StateMachine<WorkflowState, WorkflowEvent> rootStateMachine) {
        
        Optional.ofNullable(message)
                .flatMap(msg -> Optional.ofNullable((UUID) msg.getHeaders().getOrDefault("workflow_instance_id", null)))
                .flatMap(repository::findById)
                .ifPresent(instance -> {
                    // Update instance status based on the new state
                    instance.setStatus(mapStateToStatus(state.getId()));
                    repository.save(instance);
                });
    }

    private StepStatus mapStateToStatus(WorkflowState state) {
        switch (state) {
            case IN_PROGRESS: return StepStatus.IN_PROGRESS;
            case PENDING_APPROVAL: return StepStatus.PENDING;
            case APPROVED: return StepStatus.APPROVED;
            case REJECTED: return StepStatus.REJECTED;
            case COMPLETED: return StepStatus.COMPLETED;
            case FAILED: return StepStatus.FAILED;
            default: return StepStatus.PENDING;
        }
    }
}
