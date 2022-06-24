package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EntityScan
@RestController
@RequestMapping(UserRestController.PATH)
public class UserRestController {
    public static final String PATH = "/api/users";

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user/")
    public User createUser(@RequestBody User.CreateUserRequestDto requestDto) {
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
