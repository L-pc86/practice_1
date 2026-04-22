package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.test1.common.Result;
import org.example.test1.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@Slf4j
@Tag(name = "数据统计接口", description = "后台管理数据看板")
public class ReportController {

    @Autowired
    private IReportService reportService;

    @Operation(summary = "营业额统计", description = "按日期范围统计每日营业额")
    @GetMapping("/turnoverStatistics")
    public Result<Map<String, Object>> turnoverStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计: {} ~ {}", begin, end);
        Map<String, Object> data = reportService.getTurnoverStatistics(begin, end);
        return Result.success(data);
    }

    @Operation(summary = "用户统计", description = "按日期范围统计每日新增用户和总用户数")
    @GetMapping("/userStatistics")
    public Result<Map<String, Object>> userStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("用户统计: {} ~ {}", begin, end);
        Map<String, Object> data = reportService.getUserStatistics(begin, end);
        return Result.success(data);
    }

    @Operation(summary = "订单统计", description = "按日期范围统计每日订单数和完成率")
    @GetMapping("/ordersStatistics")
    public Result<Map<String, Object>> ordersStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计: {} ~ {}", begin, end);
        Map<String, Object> data = reportService.getOrderStatistics(begin, end);
        return Result.success(data);
    }

    @Operation(summary = "销量排名Top10", description = "按日期范围统计菜品销量排名前10")
    @GetMapping("/top10")
    public Result<List<Map<String, Object>>> top10(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("销量排名Top10: {} ~ {}", begin, end);
        List<Map<String, Object>> data = reportService.getSalesTop10(begin, end);
        return Result.success(data);
    }

    @Operation(summary = "获取今日运营数据", description = "工作台页面：今日营业额、订单数、完成率、新增用户数")
    @GetMapping("/workspace")
    public Result<Map<String, Object>> workspace() {
        log.info("获取今日运营数据");
        Map<String, Object> data = reportService.getWorkspaceData();
        return Result.success(data);
    }

    @Operation(summary = "各状态订单数量", description = "统计待接单、已接单、派送中的订单数量")
    @GetMapping("/orderCountByStatus")
    public Result<Map<String, Object>> orderCountByStatus() {
        log.info("查询各状态订单数量");
        Map<String, Object> data = reportService.getOrderCountByStatus();
        return Result.success(data);
    }
}
