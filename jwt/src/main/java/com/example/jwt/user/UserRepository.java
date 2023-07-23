package com.example.jwt.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    //Optional puede devolver un User o null
    Optional<User> findByUsername(String username);
}
