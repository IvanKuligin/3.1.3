package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    User update(User user);
    void deleteById(Long id);

    Optional<User> findById(Long id);
    Optional<User> findByIdWithRoles(Long id);

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameWithRoles(String username);

    List<User> findAll();
    List<User> findAllWithRoles();
}
