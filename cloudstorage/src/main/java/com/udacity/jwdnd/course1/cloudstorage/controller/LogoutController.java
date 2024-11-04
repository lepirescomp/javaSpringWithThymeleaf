package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping()
    public String logoutView(Authentication authentication) {
        authentication.setAuthenticated(false);
        return "login";
    }
}
