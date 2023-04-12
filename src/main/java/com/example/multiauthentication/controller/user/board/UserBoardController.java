package com.example.multiauthentication.controller.user.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user/board")
public class UserBoardController {

    @GetMapping("/list")
    public String list() {
        return "user/board/list";
    }
}
