package com.workflow.engine.controller;

import com.workflow.engine.domain.Workflow;
import com.workflow.engine.domain.WorkflowInstance;
import com.workflow.engine.domain.WorkflowStatus;
import com.workflow.engine.repository.WorkflowRepository;
import com.workflow.engine.service.WorkflowExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workflows")
@CrossOrigin(origins = "*")
public class WorkflowController {

    private final WorkflowExecutionService executionService;
    private final WorkflowRepository workflowRepository;

    public WorkflowController(WorkflowExecutionService executionService, WorkflowRepository workflowRepository) {
        this.executionService = executionService;
        this.workflowRepository = workflowRepository;
    }

    @PostMapping
    public ResponseEntity<Workflow> createWorkflow(@RequestBody Workflow workflow, @RequestHeader("X-Tenant-Id") UUID tenantId, @RequestHeader("X-User-Id") UUID userId) {
        workflow.setTenantId(tenantId);
        workflow.setCreatedBy(userId);
        workflow.setStatus(WorkflowStatus.DRAFT);
        return ResponseEntity.ok(workflowRepository.save(workflow));
    }

    @GetMapping
    public ResponseEntity<List<Workflow>> listWorkflows(@RequestHeader("X-Tenant-Id") UUID tenantId) {
        return ResponseEntity.ok(workflowRepository.findByTenantId(tenantId));
    }

    @PostMapping("/{workflowId}/execute")
    public ResponseEntity<WorkflowInstance> executeWorkflow(
            @PathVariable UUID workflowId, 
            @RequestHeader("X-User-Id") UUID userId) {
        
        WorkflowInstance instance = executionService.startWorkflow(workflowId, userId);
        return ResponseEntity.ok(instance);
    }

    @PostMapping("/instances/{instanceId}/approve")
    public ResponseEntity<WorkflowInstance> approveStep(@PathVariable UUID instanceId) {
        WorkflowInstance instance = executionService.approveStep(instanceId);
        return ResponseEntity.ok(instance);
    }
    
    @PostMapping("/instances/{instanceId}/reject")
    public ResponseEntity<WorkflowInstance> rejectStep(@PathVariable UUID instanceId) {
        WorkflowInstance instance = executionService.rejectStep(instanceId);
        return ResponseEntity.ok(instance);
    }
}
