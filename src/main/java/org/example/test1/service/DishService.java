package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.Dish;
import org.example.test1.mapper.DishMapper;
import org.springframework.stereotype.Service;

@Service
public class DishService extends ServiceImpl<DishMapper, Dish> implements IDishService {
}
