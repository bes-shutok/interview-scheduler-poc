package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.dto.CreateUserRequestDto;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping(UserRestController.PATH)
public class UserRestController {
    public static final String PATH = "/api/users";

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user/")
    public User createUser(@RequestBody CreateUserRequestDto requestDto) {
        return userRepository.save(new User(requestDto));
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
