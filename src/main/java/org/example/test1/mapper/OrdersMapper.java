package org.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.test1.entity.Orders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 按日期统计营业额（只统计已完成订单）
     */
    @Select("SELECT DATE(order_time) AS date, IFNULL(SUM(amount), 0) AS turnover " +
            "FROM orders WHERE status = 5 AND order_time >= #{begin} AND order_time <= #{end} " +
            "GROUP BY DATE(order_time) ORDER BY date")
    List<Map<String, Object>> sumTurnoverByDateRange(@Param("begin") LocalDateTime begin,
                                                     @Param("end") LocalDateTime end);

    /**
     * 按日期统计订单数
     */
    @Select("SELECT DATE(order_time) AS date, COUNT(*) AS totalCount, " +
            "SUM(CASE WHEN status = 5 THEN 1 ELSE 0 END) AS validCount " +
            "FROM orders WHERE order_time >= #{begin} AND order_time <= #{end} " +
            "GROUP BY DATE(order_time) ORDER BY date")
    List<Map<String, Object>> countOrdersByDateRange(@Param("begin") LocalDateTime begin,
                                                     @Param("end") LocalDateTime end);

    /**
     * 统计各状态订单数量
     */
    @Select("SELECT status, COUNT(*) AS count FROM orders GROUP BY status")
    List<Map<String, Object>> countByStatus();

    /**
     * 销量排名Top10（只统计已完成订单）
     */
    @Select("SELECT od.name, SUM(od.copies) AS number " +
            "FROM order_detail od LEFT JOIN orders o ON od.order_id = o.id " +
            "WHERE o.status = 5 AND o.order_time >= #{begin} AND o.order_time <= #{end} " +
            "GROUP BY od.name ORDER BY number DESC LIMIT 10")
    List<Map<String, Object>> getSalesTop10(@Param("begin") LocalDateTime begin,
                                            @Param("end") LocalDateTime end);

    /**
     * 统计指定日期范围内的总营业额
     */
    @Select("SELECT IFNULL(SUM(amount), 0) FROM orders WHERE status = 5 " +
            "AND order_time >= #{begin} AND order_time <= #{end}")
    BigDecimal sumAmountByRange(@Param("begin") LocalDateTime begin,
                                @Param("end") LocalDateTime end);

    /**
     * 统计指定日期范围内的总订单数
     */
    @Select("SELECT COUNT(*) FROM orders WHERE order_time >= #{begin} AND order_time <= #{end}")
    Integer countTotalByRange(@Param("begin") LocalDateTime begin,
                              @Param("end") LocalDateTime end);

    /**
     * 统计指定日期范围内的有效订单数（已完成）
     */
    @Select("SELECT COUNT(*) FROM orders WHERE status = 5 " +
            "AND order_time >= #{begin} AND order_time <= #{end}")
    Integer countValidByRange(@Param("begin") LocalDateTime begin,
                              @Param("end") LocalDateTime end);
}
