package ru.dyakov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIAuthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/homepage")
    public String homepage(Model model) {
        model.addAttribute("phoneNumber", "number here");
        model.addAttribute("balance", "money here");
        model.addAttribute("bankAccountNumber", "b accounbt number here");
        return "homepage";
    }
}
