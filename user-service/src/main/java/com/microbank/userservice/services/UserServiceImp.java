package com.microbank.userservice.services;

import com.microbank.userservice.entities.Role;
import com.microbank.userservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.microbank.userservice.entities.User;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImp implements UserService{

    private final UserRepository userRepository;

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> findAllPaged(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchByUsernameOrRole(String keyword, Pageable pageable) {
        Role role = null;
        try {
            role = Role.valueOf(keyword.toUpperCase());
        } catch (IllegalArgumentException e) {
        }
        return userRepository.findByUsernameContainingIgnoreCaseOrRole(keyword, role, pageable);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

}