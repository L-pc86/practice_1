package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.test1.common.Result;
import org.example.test1.entity.ShoppingCart;
import org.example.test1.service.IShoppingCartService;
import org.example.test1.common.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
@Tag(name = "购物车管理", description = "购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Operation(summary = "添加购物车", description = "将菜品或套餐加入购物车")
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        ShoppingCart cart = shoppingCartService.addToCart(shoppingCart, userId);
        return Result.success(cart);
    }

    @Operation(summary = "减少购物车商品", description = "减少购物车中商品数量")
    @PostMapping("/sub")
    public Result<String> sub(@RequestParam(required = false) Long dishId,
                              @RequestParam(required = false) Long setmealId,
                              @RequestParam(required = false) String dishFlavor,
                              HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        shoppingCartService.subFromCart(userId, dishId, setmealId, dishFlavor);
        return Result.success("操作成功");
    }

    @Operation(summary = "查看购物车", description = "获取当前用户购物车列表")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        List<ShoppingCart> list = shoppingCartService.listByUserId(userId);
        return Result.success(list);
    }

    @Operation(summary = "清空购物车", description = "清空当前用户购物车")
    @DeleteMapping("/clean")
    public Result<String> clean(HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        shoppingCartService.cleanCart(userId);
        return Result.success("清空购物车成功");
    }
}
