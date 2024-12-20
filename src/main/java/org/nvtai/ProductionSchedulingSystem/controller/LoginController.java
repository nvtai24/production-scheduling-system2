package org.nvtai.ProductionSchedulingSystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(Principal principal, @RequestParam(value = "error", required = false) String error, Model model) {

//        if (principal != null) {
//            return "redirect:/home";
//        }

        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "login";
    }


}