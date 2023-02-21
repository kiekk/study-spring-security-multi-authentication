package com.example.multiauthentication.controller.admin.sign;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/sign")
public class AdminSignController {

    @RequestMapping(value = {"", "/"})
    public String index() {
        return "redirect:/admin/sign/in";
    }

    @RequestMapping("in")
    public String in() {
        return "admin/sign-in";
    }
}
