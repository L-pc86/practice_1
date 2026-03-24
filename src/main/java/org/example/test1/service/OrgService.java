package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.Org;
import org.example.test1.mapper.OrgMapper;
import org.springframework.stereotype.Service;

@Service
public class OrgService extends ServiceImpl<OrgMapper, Org> implements IOrgService {
}
