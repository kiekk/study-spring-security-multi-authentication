package com.example.multiauthentication.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminRootController {

    @GetMapping(value = {"", "/"})
    public String index() {
        return "redirect:/admin/sign";
    }
}
