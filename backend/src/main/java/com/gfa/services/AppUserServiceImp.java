package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDto;
import com.gfa.dtos.responsedtos.LoginResponseDto;
import com.gfa.models.AppUser;
import com.gfa.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserServiceImp implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Autowired
    public AppUserServiceImp(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public LoginResponseDto userLogin(Optional<LoginRequestDto> loginRequestDto) {
        LoginRequestDto payload = loginRequestDto.orElseThrow(() -> new NullPointerException("Input body was not received."));
        if(payload.getLoginInput() == null || payload.getLoginInput().isEmpty()) {
            throw new IllegalArgumentException("Plesae provide a name or an email address.");
        }
        if(payload.getPassword() == null || payload.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Please provide a password.");
        }
        AppUser appUser = appUserRepository.findByUsernameOrEmail(payload.getLoginInput(), payload.getLoginInput())
                .orElseThrow(() -> new NullPointerException("The user can not be found in the database."));
        if (!appUser.getPassword().equals(payload.getPassword()))
            throw new IllegalArgumentException("The password is incorrect.");
        return new LoginResponseDto("Demo token");
    }
}

