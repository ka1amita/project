package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDTO;
import com.gfa.dtos.responsedtos.LoginResponseDTO;
import com.gfa.models.AppUser;
import com.gfa.repositories.AppUserRepository;
import com.gfa.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginUserServiceImp implements LoginUserService {

    private final AppUserRepository appUserRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public LoginUserServiceImp(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

//    @Override
//    public AppUser saveUser(AppUser appUser) {
//        return appUserRepository.save(appUser);
//    }
//
//    @Override
//    public Role saveRole(Role role) {
//        return roleRepository.save(role);
//    }
//
//    @Override
//    public void addRoleToUser(String username, String roleName) {
//        AppUser appUser = appUserRepository.findByUsername(username);
//        Role role = roleRepository.findByName(roleName);
//        appUser.getRoles().add(role);
//        appUserRepository.save(appUser);
//    }
//
//    @Override
//    public AppUser getUser(String username) {
//        return appUserRepository.findByUsername(username);
//    }
//
//    @Override
//    public List<AppUser> getUsers() {
//        return appUserRepository.findAll();
//    }

    @Override
    public LoginResponseDTO userLogin(Optional<LoginRequestDTO> loginRequestDto) {
        LoginRequestDTO payload = loginRequestDto.orElseThrow(() -> new NullPointerException("Input body was not received."));
        if (payload.getLoginInput() == null || payload.getLoginInput().isEmpty()) {
            throw new IllegalArgumentException("Please provide a name or an email address.");
        }
        if (payload.getPassword() == null || payload.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Please provide a password.");
        }
        AppUser appUser = appUserRepository.findByEmailContainsAndUsernameContains(payload.getLoginInput(), payload.getLoginInput())
                .orElseThrow(() -> new NullPointerException("The user can not be found in the database."));
        if (!appUser.getPassword().equals(payload.getPassword()))
            throw new IllegalArgumentException("The password is incorrect.");
        return new LoginResponseDTO("Demo token");
    }
}

