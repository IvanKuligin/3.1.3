package ru.kata.spring.boot_security.demo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner{
    private final RoleService roleService;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public DataLoader(RoleService roleService, UserService userService, PasswordEncoder encoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Role roleUser = roleService.findByName("ROLE_USER").orElseGet(() -> roleService.save(new Role("ROLE_USER")));
        Role roleAdmin = roleService.findByName("ROLE_ADMIN").orElseGet(() -> roleService.save(new Role("ROLE_ADMIN")));

        userService.findByUsername("admin").orElseGet(() -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setFirstName("Admin");
            admin.setLastName("Root");
            admin.setRoles(Set.of(roleAdmin, roleUser));
            return userService.save(admin);
        });

        userService.findByUsername("user").orElseGet(() -> {
            User user = new User();
            user.setUsername("user");
            user.setPassword("user");
            user.setFirstName("Ivan");
            user.setLastName("Userov");
            user.setRoles(Set.of(roleUser));
            return userService.save(user);
        });
    }
}
