package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Orders;
import org.example.test1.mapper.OrdersMapper;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Override
    public void submit(Orders orders, Long userId) {
        orders.setUserId(userId);
        orders.setOrderNumber(UUID.randomUUID().toString().replace("-", ""));
        orders.setStatus(1);
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

    @Override
    public Page<Orders> adminPageQuery(Integer page, Integer pageSize, Long userId, Integer status) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(userId != null, Orders::getUserId, userId);
        wrapper.eq(status != null, Orders::getStatus, status);
        wrapper.orderByDesc(Orders::getOrderTime);
        page(pageInfo, wrapper);
        return pageInfo;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
        orders.setStatus(status);
        updateById(orders);
    }

    @Override
    public Orders getOrderDetail(Long id) {
        return getById(id);
    }

    @Override
    public void reminder(Long id) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
    }
}
