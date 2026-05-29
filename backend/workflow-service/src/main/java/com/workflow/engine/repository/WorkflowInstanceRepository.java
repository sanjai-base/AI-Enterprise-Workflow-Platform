package com.workflow.engine.repository;

import com.workflow.engine.domain.WorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, UUID> {
    List<WorkflowInstance> findByWorkflowId(UUID workflowId);
}
