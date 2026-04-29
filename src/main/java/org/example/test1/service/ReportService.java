package org.example.test1.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.test1.mapper.OrdersMapper;
import org.example.test1.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        List<LocalDate> dateList = getDateList(begin, end);

        List<Map<String, Object>> dbResult = ordersMapper.sumTurnoverByDateRange(beginTime, endTime);
        Map<String, Object> turnoverMap = dbResult.stream()
                .collect(Collectors.toMap(
                        m -> m.get("date").toString(),
                        m -> m.get("turnover")
                ));

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

        BigDecimal turnover = ordersMapper.sumAmountByRange(beginTime, endTime);
        Integer totalOrders = ordersMapper.countTotalByRange(beginTime, endTime);
        Integer validOrders = ordersMapper.countValidByRange(beginTime, endTime);

        double completionRate = (totalOrders == null || totalOrders == 0) ? 0.0 :
                BigDecimal.valueOf(validOrders)
                        .divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                        .doubleValue();

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

        result.put("toBeConfirmed", 0);
        result.put("confirmed", 0);
        result.put("deliveryInProgress", 0);

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

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate end = LocalDate.now().minusDays(1);
        LocalDate begin = end.minusDays(29);

        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        Map<String, Object> turnoverData = getTurnoverStatistics(begin, end);
        Map<String, Object> userData = getUserStatistics(begin, end);
        Map<String, Object> orderData = getOrderStatistics(begin, end);
        List<Map<String, Object>> top10Data = getSalesTop10(begin, end);

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("运营数据报表");

            XSSFRow row0 = sheet.createRow(0);
            row0.createCell(0).setCellValue("运营数据报表");
            row0.createCell(1).setCellValue("时间：" + begin + " 至 " + end);

            XSSFRow row1 = sheet.createRow(2);
            row1.createCell(0).setCellValue("概览数据");
            XSSFRow row2 = sheet.createRow(3);
            row2.createCell(0).setCellValue("营业额");
            row2.createCell(1).setCellValue(orderData.get("totalOrderCount") != null ? orderData.get("totalOrderCount").toString() : "0");
            XSSFRow row3 = sheet.createRow(4);
            row3.createCell(0).setCellValue("有效订单数");
            row3.createCell(1).setCellValue(orderData.get("validOrderCount") != null ? orderData.get("validOrderCount").toString() : "0");
            XSSFRow row4 = sheet.createRow(5);
            row4.createCell(0).setCellValue("订单完成率");
            row4.createCell(1).setCellValue(orderData.get("orderCompletionRate") != null ? orderData.get("orderCompletionRate").toString() : "0");
            XSSFRow row5 = sheet.createRow(6);
            row5.createCell(0).setCellValue("新增用户数");
            row5.createCell(1).setCellValue(userData.get("totalUserList") != null ? userData.get("totalUserList").toString() : "0");

            XSSFRow row7 = sheet.createRow(8);
            row7.createCell(0).setCellValue("日期");
            row7.createCell(1).setCellValue("营业额");
            row7.createCell(2).setCellValue("有效订单数");
            row7.createCell(3).setCellValue("订单完成率");
            row7.createCell(4).setCellValue("新增用户数");

            String[] dates = turnoverData.get("dateList") != null ? turnoverData.get("dateList").toString().split(",") : new String[0];
            String[] turnovers = turnoverData.get("turnoverList") != null ? turnoverData.get("turnoverList").toString().split(",") : new String[0];
            String[] validOrders = orderData.get("validOrderCountList") != null ? orderData.get("validOrderCountList").toString().split(",") : new String[0];
            String[] newUsers = userData.get("newUserList") != null ? userData.get("newUserList").toString().split(",") : new String[0];

            for (int i = 0; i < dates.length; i++) {
                XSSFRow row = sheet.createRow(9 + i);
                row.createCell(0).setCellValue(dates[i]);
                row.createCell(1).setCellValue(i < turnovers.length ? turnovers[i] : "0");
                row.createCell(2).setCellValue(i < validOrders.length ? validOrders[i] : "0");
                row.createCell(3).setCellValue(orderData.get("orderCompletionRate") != null ? orderData.get("orderCompletionRate").toString() : "0");
                row.createCell(4).setCellValue(i < newUsers.length ? newUsers[i] : "0");
            }

            int top10StartRow = 9 + dates.length + 1;
            XSSFRow top10Header = sheet.createRow(top10StartRow);
            top10Header.createCell(0).setCellValue("销量排名Top10");
            XSSFRow top10Title = sheet.createRow(top10StartRow + 1);
            top10Title.createCell(0).setCellValue("商品名称");
            top10Title.createCell(1).setCellValue("销量");

            for (int i = 0; i < top10Data.size(); i++) {
                XSSFRow row = sheet.createRow(top10StartRow + 2 + i);
                Map<String, Object> item = top10Data.get(i);
                row.createCell(0).setCellValue(item.get("name") != null ? item.get("name").toString() : "");
                row.createCell(1).setCellValue(item.get("number") != null ? item.get("number").toString() : "0");
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=operational_data_" +
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx");

            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            log.error("导出运营数据报表失败", e);
        }
    }

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
