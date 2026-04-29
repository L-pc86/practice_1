package org.example.test1.service;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IReportService {

    Map<String, Object> getTurnoverStatistics(LocalDate begin, LocalDate end);

    Map<String, Object> getUserStatistics(LocalDate begin, LocalDate end);

    Map<String, Object> getOrderStatistics(LocalDate begin, LocalDate end);

    List<Map<String, Object>> getSalesTop10(LocalDate begin, LocalDate end);

    Map<String, Object> getWorkspaceData();

    Map<String, Object> getOrderCountByStatus();

    void exportBusinessData(HttpServletResponse response);
}
