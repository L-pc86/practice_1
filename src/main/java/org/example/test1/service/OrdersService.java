package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.Orders;
import org.example.test1.mapper.OrdersMapper;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Override
    public void submit(Orders orders) {
        orders.setOrderNumber(UUID.randomUUID().toString().replace("-", ""));
        save(orders);
    }

    @Override
    public Page<Orders> pageQuery(Long userId, Integer page, Integer pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, Orders::getUserId, userId);
        wrapper.orderByDesc(Orders::getOrderTime);
        page(pageInfo, wrapper);
        return pageInfo;
    }
}
