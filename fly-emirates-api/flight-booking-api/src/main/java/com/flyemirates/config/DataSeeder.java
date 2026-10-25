package com.flyemirates.config;

import com.flyemirates.entity.Role;
import com.flyemirates.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
	//CommandLineRunner is springbooot interface  jo bean implement karel teva ha interface run honar if application start and check ki role exist kartoy ka naytr add kara
	
    private final RoleRepository roleRepository;

    public DataSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        seedRole("ADMIN");
        seedRole("STAFF");
        seedRole("CUSTOMER");
    }

    private void seedRole(String name) {
        if (roleRepository.findByName(name).isEmpty()) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
            System.out.println("Seeded role: " + name);
        }
    }
}