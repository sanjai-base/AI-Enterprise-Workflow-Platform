package com.workflow.user.repository;

import com.workflow.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findByTenantId(UUID tenantId);
    List<User> findByDepartmentAndTenantId(String department, UUID tenantId);
}
