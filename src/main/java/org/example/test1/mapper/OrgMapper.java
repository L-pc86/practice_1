package org.example.test1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.test1.entity.Org;

@Mapper
public interface OrgMapper extends BaseMapper<Org> {
}
