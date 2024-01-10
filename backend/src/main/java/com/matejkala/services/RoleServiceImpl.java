package com.matejkala.services;

import com.matejkala.models.Role;
import com.matejkala.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElseThrow(()
                -> new NoSuchElementException("Couldn't find the role in our database"));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
