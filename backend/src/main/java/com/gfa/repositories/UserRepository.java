package com.gfa.repositories;

import com.gfa.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    AppUser findByUsername(String username);

    AppUser findByEmail(String email);

}
