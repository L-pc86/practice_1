package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.test1.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/shop")
@Slf4j
@Tag(name = "店铺相关接口", description = "营业状态管理")
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Operation(summary = "设置店铺营业状态", description = "管理端：1为营业中，0为打烊")
    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success("店铺状态设置成功");
    }

    @Operation(summary = "获取店铺营业状态", description = "通用：获取当前店铺状态(1营业, 0打烊)")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取到店铺营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status == null ? 0 : status); // 默认未设置时为打烊或可以设为1营业
    }
}
