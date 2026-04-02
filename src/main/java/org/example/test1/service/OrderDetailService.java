package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.OrderDetail;
import org.example.test1.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderDetailService extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    @Override
    public List<OrderDetail> listByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        return list(wrapper);
    }

    @Override
    public void saveBatch(List<OrderDetail> orderDetails) {
        saveBatch(orderDetails);
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderDetail::getOrderId, orderId);
        remove(wrapper);
    }

    @Override
    public List<OrderDetail> listByOrderIds(List<Long> orderIds) {
        LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderDetail::getOrderId, orderIds);
        return list(wrapper);
    }
}
