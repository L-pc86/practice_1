package org.example.test1.service;

import lombok.extern.slf4j.Slf4j;
import org.example.test1.mapper.OrdersMapper;
import org.example.test1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportService implements IReportService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, Object> getTurnoverStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 生成日期列表
        List<LocalDate> dateList = getDateList(begin, end);

        // 查询每日营业额
        List<Map<String, Object>> dbResult = ordersMapper.sumTurnoverByDateRange(beginTime, endTime);
        Map<String, Object> turnoverMap = dbResult.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> m.get("turnover")
                ));

        // 拼装结果，没有数据的日期补0
        List<String> dateStrList = dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
        List<BigDecimal> turnoverList = dateList.stream()
                .map(date -> {
                    Object val = turnoverMap.get(date.toString());
                    return val != null ? new BigDecimal(val.toString()) : BigDecimal.ZERO;
                })
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("dateList", String.join(",", dateStrList));
        result.put("turnoverList", turnoverList.stream().map(BigDecimal::toString).collect(Collectors.joining(",")));
        return result;
    }

    @Override
    public Map<String, Object> getUserStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<LocalDate> dateList = getDateList(begin, end);

        // 查询每日新增用户
        List<Map<String, Object>> dbResult = userMapper.countNewUsersByDateRange(beginTime, endTime);
        Map<String, Object> newUserMap = dbResult.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> m.get("newUsers")
                ));

        List<String> dateStrList = dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            Object val = newUserMap.get(date.toString());
            int newUsers = val != null ? Integer.parseInt(val.toString()) : 0;
            newUserList.add(newUsers);

            // 统计截止当日总用户数
            Integer totalUsers = userMapper.countTotalUsersBeforeDate(LocalDateTime.of(date, LocalTime.MAX));
            totalUserList.add(totalUsers != null ? totalUsers : 0);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("dateList", String.join(",", dateStrList));
        result.put("newUserList", newUserList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        result.put("totalUserList", totalUserList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return result;
    }

    @Override
    public Map<String, Object> getOrderStatistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<LocalDate> dateList = getDateList(begin, end);

        // 查询每日订单数
        List<Map<String, Object>> dbResult = ordersMapper.countOrdersByDateRange(beginTime, endTime);
        Map<String, Map<String, Object>> orderMap = dbResult.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> m
                ));

        List<String> dateStrList = dateList.stream().map(LocalDate::toString).collect(Collectors.toList());
        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();
        int totalOrderSum = 0;
        int validOrderSum = 0;

        for (LocalDate date : dateList) {
            Map<String, Object> dayData = orderMap.get(date.toString());
            int total = dayData != null ? Integer.parseInt(dayData.get("totalCount").toString()) : 0;
            int valid = dayData != null ? Integer.parseInt(dayData.get("validCount").toString()) : 0;
            totalOrderList.add(total);
            validOrderList.add(valid);
            totalOrderSum += total;
            validOrderSum += valid;
        }

        double completionRate = totalOrderSum == 0 ? 0.0 :
                BigDecimal.valueOf(validOrderSum)
                        .divide(BigDecimal.valueOf(totalOrderSum), 2, RoundingMode.HALF_UP)
                        .doubleValue();

        Map<String, Object> result = new HashMap<>();
        result.put("dateList", String.join(",", dateStrList));
        result.put("totalOrderCountList", totalOrderList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        result.put("validOrderCountList", validOrderList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        result.put("totalOrderCount", totalOrderSum);
        result.put("validOrderCount", validOrderSum);
        result.put("orderCompletionRate", completionRate);
        return result;
    }

    @Override
    public List<Map<String, Object>> getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        return ordersMapper.getSalesTop10(beginTime, endTime);
    }

    @Override
    public Map<String, Object> getWorkspaceData() {
        LocalDateTime beginTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 今日营业额
        BigDecimal turnover = ordersMapper.sumAmountByRange(beginTime, endTime);

        // 今日总订单数
        Integer totalOrders = ordersMapper.countTotalByRange(beginTime, endTime);

        // 今日有效订单数（已完成）
        Integer validOrders = ordersMapper.countValidByRange(beginTime, endTime);

        // 订单完成率
        double completionRate = (totalOrders == null || totalOrders == 0) ? 0.0 :
                BigDecimal.valueOf(validOrders)
                        .divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                        .doubleValue();

        // 今日新增用户数
        Integer newUsers = userMapper.countNewUsersByRange(beginTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("turnover", turnover != null ? turnover : BigDecimal.ZERO);
        result.put("validOrderCount", validOrders != null ? validOrders : 0);
        result.put("totalOrderCount", totalOrders != null ? totalOrders : 0);
        result.put("orderCompletionRate", completionRate);
        result.put("newUsers", newUsers != null ? newUsers : 0);
        return result;
    }

    @Override
    public Map<String, Object> getOrderCountByStatus() {
        List<Map<String, Object>> statusList = ordersMapper.countByStatus();
        Map<String, Object> result = new HashMap<>();

        // 初始化所有状态为0
        result.put("toBeConfirmed", 0);   // 待接单 status=2
        result.put("confirmed", 0);        // 已接单 status=3
        result.put("deliveryInProgress", 0); // 派送中 status=4

        for (Map<String, Object> item : statusList) {
            int status = Integer.parseInt(item.get("status").toString());
            int count = Integer.parseInt(item.get("count").toString());
            switch (status) {
                case 2 -> result.put("toBeConfirmed", count);
                case 3 -> result.put("confirmed", count);
                case 4 -> result.put("deliveryInProgress", count);
            }
        }
        return result;
    }

    /**
     * 生成从 begin 到 end 的日期列表
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate current = begin;
        while (!current.isAfter(end)) {
            dateList.add(current);
            current = current.plusDays(1);
        }
        return dateList;
    }
}
