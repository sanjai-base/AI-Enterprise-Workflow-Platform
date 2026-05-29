package com.workflow.auth.config;

import com.workflow.auth.domain.Role;
import com.workflow.auth.domain.Tenant;
import com.workflow.auth.domain.User;
import com.workflow.auth.repository.TenantRepository;
import com.workflow.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (tenantRepository.count() == 0) {
            System.out.println("Seeding default Tenant and Admin User...");
            
            Tenant tenant = new Tenant();
            tenant.setName("Acme Corp");
            tenant.setPlan("ENTERPRISE");
            tenant = tenantRepository.save(tenant);

            User admin = new User();
            admin.setEmail("admin@acme.com");
            admin.setPasswordHash(passwordEncoder.encode("admin"));
            admin.setRole(Role.SUPER_ADMIN);
            admin.setDepartment("IT");
            admin.setTenantId(tenant.getId());
            
            userRepository.save(admin);
            
            System.out.println("Default user created: admin@acme.com / admin");
        }
    }
}
