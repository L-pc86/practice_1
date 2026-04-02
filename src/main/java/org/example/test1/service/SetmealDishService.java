package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.SetmealDish;
import org.example.test1.mapper.SetmealDishMapper;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishService extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {
}
