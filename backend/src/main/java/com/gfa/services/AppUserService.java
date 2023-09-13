package com.gfa.services;

import com.gfa.dtos.requestdtos.LoginRequestDto;
import com.gfa.dtos.responsedtos.LoginResponseDto;

import java.util.Optional;

public interface AppUserService {

    LoginResponseDto userLogin(Optional<LoginRequestDto> loginRequestDto);
}
