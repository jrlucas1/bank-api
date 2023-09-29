package com.example.bank.domain.user;

public record RegisterDTO(String login, String password, UserRole role) {
}