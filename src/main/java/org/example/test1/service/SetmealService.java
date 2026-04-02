package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.Setmeal;
import org.example.test1.mapper.SetmealMapper;
import org.springframework.stereotype.Service;

@Service
public class SetmealService extends ServiceImpl<SetmealMapper, Setmeal> implements ISetmealService {
}
