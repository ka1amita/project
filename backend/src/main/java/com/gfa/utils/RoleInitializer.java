package com.gfa.utils;

import com.gfa.models.Role;
import com.gfa.models.RoleType;
import com.gfa.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RoleInitializer {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (!roleRepository.findByName(roleType.getName()).isPresent()) {
                Role role = new Role(roleType.getName());
                roleRepository.save(role);
            }
        }
    }
}