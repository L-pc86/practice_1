package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.test1.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/report")
@Slf4j
@Tag(name = "数据统计接口", description = "后台管理数据看板")
public class ReportController {

    @Operation(summary = "获取今日运营数据", description = "用于外卖后台的工作台页面大屏显示，包含营业额、订单数等")
    @GetMapping("/workspace")
    public Result<Map<String, Object>> workspace() {
        // 这里通常需要调用 ReportService 或 OrderMapper 去做一个复杂的数据库聚合查询
        // 当前为了完善你的接口结构，此处做一个 Mock 返回

        Map<String, Object> workspaceData = new HashMap<>();
        workspaceData.put("turnover", 3800.50);          // 今日营业额
        workspaceData.put("validOrderCount", 125);       // 有效订单数
        workspaceData.put("orderCompletionRate", 0.95);  // 订单完成率
        workspaceData.put("newUsers", 18);               // 新增用户数
        
        log.info("获取 {} 的运营总览数据", LocalDateTime.now());
        
        return Result.success(workspaceData);
    }
}
