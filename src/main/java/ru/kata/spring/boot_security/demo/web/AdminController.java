package ru.kata.spring.boot_security.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService,
                           RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminIndex(Model model) {
        List<User> users = userService.findAll();
        List<Role> roles = roleService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "admin-form";
    }

    @PostMapping
    public String createUser(@ModelAttribute User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        Set<Role> resolvedRoles = resolveRoles(roleIds);
        user.setRoles(resolvedRoles);
        userService.save(user);
        System.out.println("Назначенные роли: " + resolvedRoles);

        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findByIdWithRoles(id).orElseThrow();
        Set<Long> ids = user.getRoles().stream().map(Role::getId).collect(Collectors.toSet());
        user.setRoleIds(ids);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.findAll());
        return "admin-form";
    }

    @PostMapping("/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {

        user.setId(id);
        Set<Role> resolvedRoles = resolveRoles(roleIds);
        user.setRoles(resolvedRoles);
        userService.update(user);
        System.out.println("Назначенные роли: " + resolvedRoles);
        return "redirect:/admin";
    }


    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    private Set<Role> resolveRoles(List<Long> roleIds) {
        Set<Role> result = new HashSet<>();
        if (roleIds != null) {
            for (Long id : roleIds) {
                roleService.findById(id).ifPresent(result::add);
            }
        }
        return result;
    }
}
