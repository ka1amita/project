package com.gfa.services;

import com.gfa.exceptions.role.RoleNotFoundException;
import com.gfa.models.Role;
import com.gfa.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
                -> new RoleNotFoundException("Couldn't find the role in our database"));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
