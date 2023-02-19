package com.example.multiauthentication.security.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminRootController {

    @RequestMapping(value = {"", "/"})
    public String index() {
        return "redirect:/admin/sign";
    }
}
