package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.test1.common.Result;
import org.example.test1.common.utils.JwtUtil;
import org.example.test1.entity.User;
import org.example.test1.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "顾客用户相关接口")
public class CUserController {

    @Autowired
    private IUserService userService;

    @Operation(summary = "微信登录", description = "用户微信登录获取用户信息，返回JWT Token")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User loginUser) {
        User user = userService.wxLogin(loginUser.getPhone());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token = JwtUtil.createToken(claims);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.success(data);
    }

    @Operation(summary = "获取登录用户信息", description = "获取当前登录用户信息")
    @GetMapping("/getLoginUser")
    public Result<User> getLoginUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        User user = userService.getLoginUser(userId);
        return Result.success(user);
    }

    @Operation(summary = "修改个人信息", description = "修改用户昵称、性别、头像等个人信息")
    @PutMapping
    public Result<String> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        userService.updateUserInfo(user, userId);
        return Result.success("修改成功");
    }

    @Operation(summary = "用户登出", description = "退出当前用户登录")
    @PostMapping("/logout")
    public Result<String> logout() {
        return Result.success("退出成功");
    }
}
