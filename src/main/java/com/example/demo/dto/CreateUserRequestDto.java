package com.example.demo.dto;

import com.example.demo.domain.UserType;

public record CreateUserRequestDto(String username, String password, UserType userType) {
}
