package com.reqapp.controller;

import com.reqapp.domain.User;
import com.reqapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("isLoginPage", true);
        return "login";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isRegisterPage", true);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        if (userService.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "Username already exists.");
            model.addAttribute("isRegisterPage", true);
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/login?success";
    }

    @GetMapping("/profile")
    public String showProfileForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "oldPassword", required = false) String oldPassword,
            @RequestParam(value = "password", required = false) String password,
            Model model,
            Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (password != null && !password.isEmpty()) {
            if (oldPassword == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
                model.addAttribute("user", user);
                model.addAttribute("error", "Incorrect old password.");
                return "profile";
            }
        }

        userService.updateUserProfile(username, firstName, lastName, email, password);
        return "redirect:/profile?success";
    }
}
