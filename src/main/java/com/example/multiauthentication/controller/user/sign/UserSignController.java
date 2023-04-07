package com.example.multiauthentication.controller.user.sign;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/sign")
public class UserSignController {

    @GetMapping(value = {"", "/"})
    public String index() {
        return "redirect:/user/sign/in";
    }

    @GetMapping("in")
    public String in() {
        return "user/sign-in";
    }
}
