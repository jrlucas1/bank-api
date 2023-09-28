package com.example.bank.repositories;

import com.example.bank.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByLogin(String login);
}
