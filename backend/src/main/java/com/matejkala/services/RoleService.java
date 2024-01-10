package com.matejkala.services;

import com.matejkala.models.Role;

public interface RoleService {

    Role findByName(String name);

    Role saveRole(Role role);

}
