package org.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.test1.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 按日期统计新增用户数和截止当日的总用户数
     */
    @Select("SELECT DATE(create_time) AS date, COUNT(*) AS newUsers " +
            "FROM user WHERE create_time >= #{begin} AND create_time <= #{end} " +
            "GROUP BY DATE(create_time) ORDER BY date")
    List<Map<String, Object>> countNewUsersByDateRange(@Param("begin") LocalDateTime begin,
                                                       @Param("end") LocalDateTime end);

    /**
     * 统计截止到指定日期的总用户数
     */
    @Select("SELECT COUNT(*) FROM user WHERE create_time <= #{end}")
    Integer countTotalUsersBeforeDate(@Param("end") LocalDateTime end);

    /**
     * 统计指定日期范围内的新增用户数
     */
    @Select("SELECT COUNT(*) FROM user WHERE create_time >= #{begin} AND create_time <= #{end}")
    Integer countNewUsersByRange(@Param("begin") LocalDateTime begin,
                                 @Param("end") LocalDateTime end);
}
