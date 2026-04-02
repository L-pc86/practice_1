package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.entity.Orders;
import org.example.test1.service.IOrdersService;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/order")
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    @Autowired
    private IOrdersService ordersService;

    @Operation(summary = "用户下单", description = "提交订单")
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders, HttpServletRequest request) {
        try {
            ordersService.submit(orders);
            return Result.success("下单成功");
        } catch (Exception e) {
            log.error("下单失败", e);
            return Result.error("下单失败");
        }
    }

    @Operation(summary = "分页查询订单", description = "查询用户订单列表")
    @GetMapping("/userPage")
    public Result<Page<Orders>> userPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        Page<Orders> pageInfo = ordersService.pageQuery(userId, page, pageSize);
        return Result.success(pageInfo);
    }

    @Operation(summary = "查询订单详情", description = "根据ID查询订单详细信息")
    @GetMapping("/detail/{id}")
    public Result<Orders> detail(@PathVariable Long id) {
        Orders orders = ordersService.getById(id);
        return Result.success(orders);
    }

    @Operation(summary = "催单", description = "提醒商家尽快处理")
    @GetMapping("/reminder/{id}")
    public Result<String> reminder(@PathVariable Long id) {
        return Result.success("已催单");
    }

    @Operation(summary = "分页查询订单（管理端）", description = "分页条件查询订单列表")
    @GetMapping("/page")
    public Result<Page<Orders>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, Orders::getUserId, userId);
        wrapper.eq(status != null, Orders::getStatus, status);
        wrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    @Operation(summary = "修改订单状态", description = "更新订单状态")
    @PutMapping
    public Result<String> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(status);
        ordersService.updateById(orders);
        return Result.success("订单状态修改成功");
    }
}
