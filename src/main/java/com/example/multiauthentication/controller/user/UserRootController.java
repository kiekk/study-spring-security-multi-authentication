package com.example.multiauthentication.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserRootController {

    @RequestMapping(value = {"", "/"})
    public String index() {
        return "redirect:/user/sign";
    }
}
