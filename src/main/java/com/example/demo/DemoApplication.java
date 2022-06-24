package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EntityScan
@SpringBootApplication
@RestController
public class DemoApplication {

	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}

	@PostMapping("/user/{userName}")
	public User createUser(String userName) {
		return userRepository.save(new User(userName));
	}
	@GetMapping("/users")
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
