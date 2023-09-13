package com.gfa.services;


import com.gfa.dtos.responsedtos.RegisterRequestDTO;
import com.gfa.models.User;

public interface UserService {

    User registerUser(RegisterRequestDTO request);

    boolean isUsernameUnique(String username);

    boolean isEmailUnique(String email);

    void activateAccount (String activationCode);


}
