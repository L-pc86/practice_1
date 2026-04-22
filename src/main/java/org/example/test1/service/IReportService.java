package org.example.test1.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IReportService {

    /**
     * 营业额统计
     */
    Map<String, Object> getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     */
    Map<String, Object> getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     */
    Map<String, Object> getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 销量排名Top10
     */
    List<Map<String, Object>> getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 工作台今日运营数据
     */
    Map<String, Object> getWorkspaceData();

    /**
     * 各状态订单数量统计
     */
    Map<String, Object> getOrderCountByStatus();
}
