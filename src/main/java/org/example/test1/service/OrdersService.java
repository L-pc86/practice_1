package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.*;
import org.example.test1.mapper.OrdersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrdersService extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IAddressBookService addressBookService;

    @Autowired
    private IOrderDetailService orderDetailService;

    @Override
    @Transactional
    public void submit(Orders orders, Long userId) {
        List<ShoppingCart> cartList = shoppingCartService.listByUserId(userId);
        if (cartList == null || cartList.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.ERROR, "购物车为空，无法下单");
        }

        AddressBook addressBook = addressBookService.getAddressById(orders.getAddressId());
        if (addressBook == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "收货地址不存在");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : cartList) {
            OrderDetail detail = new OrderDetail();
            detail.setDishId(cart.getDishId());
            detail.setSetmealId(cart.getSetmealId());
            detail.setName(cart.getName());
            detail.setImage(cart.getImage());
            detail.setPrice(cart.getPrice());
            detail.setCopies(cart.getNumber());
            orderDetails.add(detail);
            totalAmount = totalAmount.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNumber())));
        }

        orders.setUserId(userId);
        orders.setOrderNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        orders.setStatus(1);
        orders.setAmount(totalAmount);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getProvinceName() + addressBook.getCityName()
                + addressBook.getDistrictName() + addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());
        save(orders);

        for (OrderDetail detail : orderDetails) {
            detail.setOrderId(orders.getId());
        }
        orderDetailService.saveBatch(orderDetails);

        shoppingCartService.cleanCart(userId);
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

    @Override
    @Transactional
    public void cancel(Long id, Long userId) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
        if (!orders.getUserId().equals(userId)) {
            throw new BusinessException(ResultCodeEnum.ERROR, "无权操作此订单");
        }
        if (orders.getStatus() > 2) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单已接单，无法取消");
        }
        orders.setStatus(6);
        updateById(orders);
    }

    @Override
    @Transactional
    public void confirm(Long id) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
        if (orders.getStatus() != 2) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单状态不正确");
        }
        orders.setStatus(3);
        updateById(orders);
    }

    @Override
    @Transactional
    public void delivery(Long id) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
        if (orders.getStatus() != 3) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单状态不正确");
        }
        orders.setStatus(4);
        updateById(orders);
    }

    @Override
    @Transactional
    public void complete(Long id) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
        if (orders.getStatus() != 4) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单状态不正确");
        }
        orders.setStatus(5);
        orders.setCheckoutTime(LocalDateTime.now());
        updateById(orders);
    }

    @Override
    @Transactional
    public void rejection(Long id) {
        Orders orders = getById(id);
        if (orders == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单不存在");
        }
        if (orders.getStatus() != 2) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单状态不正确");
        }
        orders.setStatus(6);
        updateById(orders);
    }

    @Override
    @Transactional
    public void repetition(Long id, Long userId) {
        List<OrderDetail> orderDetails = orderDetailService.listByOrderId(id);
        if (orderDetails == null || orderDetails.isEmpty()) {
            throw new BusinessException(ResultCodeEnum.ERROR, "订单明细不存在");
        }
        for (OrderDetail detail : orderDetails) {
            ShoppingCart cart = new ShoppingCart();
            cart.setUserId(userId);
            cart.setDishId(detail.getDishId());
            cart.setSetmealId(detail.getSetmealId());
            cart.setName(detail.getName());
            cart.setImage(detail.getImage());
            cart.setPrice(detail.getPrice());
            cart.setNumber(detail.getCopies());
            shoppingCartService.addToCart(cart, userId);
        }
    }
}
