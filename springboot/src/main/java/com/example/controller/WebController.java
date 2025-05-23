package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
public class WebController {

    @Resource
    private AdminService adminService;
    @Resource
    private UserService userService;


    /**
     * default request API
     */
    @GetMapping("/")
    public Result hello() {
        return Result.success();
    }

    /**
     * login
     */
    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account ac = null;
        if ("ADMIN".equals(account.getRole())) {
            ac = adminService.login(account);
        } else if ("USER".equals(account.getRole())) {
            ac = userService.login(account);
        }
        return Result.success(ac);
    }

    /**
     * register
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    /**
     * update password
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account) {
        if ("ADMIN".equals(account.getRole())) {
            adminService.updatePassword(account);
        } else if ("USER".equals(account.getRole())) {
            userService.updatePassword(account);
        }
        return Result.success();
    }

}
