package com.gfa.services;

import com.gfa.models.Role;

public interface RoleService {

    Role findByName(String name);

    Role saveRole(Role role);

}
