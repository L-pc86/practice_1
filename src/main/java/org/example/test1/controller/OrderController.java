package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.test1.common.Result;
import org.example.test1.common.utils.JwtUtil;
import org.example.test1.entity.Orders;
import org.example.test1.service.IOrdersService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

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
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        ordersService.submit(orders, userId);
        return Result.success("下单成功");
    }

    @Operation(summary = "分页查询订单", description = "查询用户订单列表")
    @GetMapping("/userPage")
    public Result<Page<Orders>> userPage(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        Page<Orders> pageInfo = ordersService.pageQuery(userId, page, pageSize);
        return Result.success(pageInfo);
    }

    @Operation(summary = "查询订单详情", description = "根据ID查询订单详细信息（含明细）")
    @GetMapping("/detail/{id}")
    public Result<Orders> detail(@PathVariable Long id) {
        Orders orders = ordersService.getOrderDetail(id);
        return Result.success(orders);
    }

    @Operation(summary = "催单", description = "提醒商家尽快处理，通过WebSocket推送通知")
    @GetMapping("/reminder/{id}")
    public Result<String> reminder(@PathVariable Long id) {
        ordersService.reminder(id);
        return Result.success("已催单");
    }

    @Operation(summary = "取消订单", description = "用户取消订单")
    @PutMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        ordersService.cancel(id, userId);
        return Result.success("取消订单成功");
    }

    @Operation(summary = "再来一单", description = "根据历史订单重新下单")
    @PostMapping("/repetition/{id}")
    public Result<String> repetition(@PathVariable Long id, HttpServletRequest request) {
        String token = request.getHeader("token");
        Long userId = JwtUtil.getUserId(token);
        ordersService.repetition(id, userId);
        return Result.success("再来一单成功");
    }

    @Operation(summary = "分页查询订单（管理端）", description = "分页条件查询订单列表，支持按订单号和日期范围搜索")
    @GetMapping("/page")
    public Result<Page<Orders>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime beginTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Page<Orders> pageInfo = ordersService.adminPageQuery(page, pageSize, userId, status,
                orderNumber, beginTime, endTime);
        return Result.success(pageInfo);
    }

    @Operation(summary = "接单", description = "商家确认接单")
    @PutMapping("/confirm/{id}")
    public Result<String> confirm(@PathVariable Long id) {
        ordersService.confirm(id);
        return Result.success("接单成功");
    }

    @Operation(summary = "拒单", description = "商家拒绝订单")
    @PutMapping("/rejection/{id}")
    public Result<String> rejection(@PathVariable Long id) {
        ordersService.rejection(id);
        return Result.success("拒单成功");
    }

    @Operation(summary = "派送", description = "商家派送订单")
    @PutMapping("/delivery/{id}")
    public Result<String> delivery(@PathVariable Long id) {
        ordersService.delivery(id);
        return Result.success("派送成功");
    }

    @Operation(summary = "完成订单", description = "完成订单")
    @PutMapping("/complete/{id}")
    public Result<String> complete(@PathVariable Long id) {
        ordersService.complete(id);
        return Result.success("订单已完成");
    }
}
