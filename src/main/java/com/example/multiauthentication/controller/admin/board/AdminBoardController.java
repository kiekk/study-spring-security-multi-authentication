package com.example.multiauthentication.controller.admin.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/board")
public class AdminBoardController {

    @GetMapping("/list")
    public String list() {
        return "admin/board/list";
    }
}
