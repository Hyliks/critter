package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.CustomerEntity;
import com.udacity.jdnd.course3.critter.entity.UserEntity;
import com.udacity.jdnd.course3.critter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<UserEntity> getByName (String name) {
        Iterable<UserEntity> users = this.userRepository.findAll();

        for(UserEntity user: users) {
            if(user.getName().equals(name)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    public Optional<UserEntity> getById(Long id) {
        return this.userRepository.findById(id);
    }
}
