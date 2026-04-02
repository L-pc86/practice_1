package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.test1.common.Result;
import org.example.test1.entity.User;
import org.example.test1.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "顾客用户相关接口")
public class CUserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "微信登录", description = "用户微信登录获取用户信息")
    @PostMapping("/login")
    public Result<User> login(@RequestBody User loginUser, HttpServletRequest request) {
        User user = userService.wxLogin(loginUser.getPhone());
        request.getSession().setAttribute("userId", user.getId());
        return Result.success(user);
    }

    @Operation(summary = "获取登录用户信息", description = "获取当前登录用户信息")
    @GetMapping("/getLoginUser")
    public Result<User> getLoginUser(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        User user = userService.getLoginUser(userId);
        return Result.success(user);
    }
}
