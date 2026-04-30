package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.test1.common.Result;
import org.example.test1.entity.Shop;
import org.example.test1.mapper.ShopMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺营业状态管理
 *
 * 当前使用数据库存储店铺状态（无需Redis）
 *
 * 启用Redis版本步骤：
 * 1. 安装Redis并启动服务
 * 2. pom.xml中取消Redis依赖的注释
 * 3. application.yml中取消Redis连接配置的注释
 * 4. 将 RedisConfig_Disabled.java 重命名为 RedisConfig.java
 * 5. 取消下方Redis版本代码的注释，并注释掉数据库版本代码
 */
@Slf4j
@RestController
@RequestMapping("/shop")
@Tag(name = "店铺相关接口", description = "营业状态管理")
public class ShopController {

    @Autowired
    private ShopMapper shopMapper;

    // ==================== 数据库版本（当前使用） ====================

    @Operation(summary = "设置店铺营业状态", description = "管理端：1为营业中，0为打烊")
    @PutMapping("/{status}")
    public Result<String> setStatus(@PathVariable Integer status) {
        log.info("设置店铺营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        Shop shop = shopMapper.selectById(1);
        if (shop == null) {
            shop = new Shop();
            shop.setId(1L);
            shop.setStatus(status);
            shopMapper.insert(shop);
        } else {
            shop.setStatus(status);
            shopMapper.updateById(shop);
        }
        return Result.success("店铺状态设置成功");
    }

    @Operation(summary = "获取店铺营业状态", description = "通用：获取当前店铺状态(1营业, 0打烊)")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Shop shop = shopMapper.selectById(1);
        Integer status = shop != null ? shop.getStatus() : 0;
        log.info("获取到店铺营业状态为：{}", status == 1 ? "营业中" : "打烊中");
        return Result.success(status);
    }

    // ==================== Redis版本（安装Redis后切换使用） ====================
    // 启用Redis后：注释掉上方数据库版本，取消下方注释即可
    //
    // public static final String KEY = "SHOP_STATUS";
    //
    // @Autowired
    // private RedisTemplate<String, Object> redisTemplate;
    //
    // @Operation(summary = "设置店铺营业状态", description = "管理端：1为营业中，0为打烊")
    // @PutMapping("/{status}")
    // public Result<String> setStatus(@PathVariable Integer status) {
    //     log.info("设置店铺营业状态为：{}", status == 1 ? "营业中" : "打烊中");
    //     redisTemplate.opsForValue().set(KEY, status);
    //     return Result.success("店铺状态设置成功");
    // }
    //
    // @Operation(summary = "获取店铺营业状态", description = "通用：获取当前店铺状态(1营业, 0打烊)")
    // @GetMapping("/status")
    // public Result<Integer> getStatus() {
    //     Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
    //     log.info("获取到店铺营业状态为：{}", status == 1 ? "营业中" : "打烊中");
    //     return Result.success(status == null ? 0 : status);
    // }
}
