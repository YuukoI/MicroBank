package com.microbank.authservice.service;

import com.microbank.authservice.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    User saveUser(User user);

}