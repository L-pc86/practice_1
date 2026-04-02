package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Orders;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface IOrdersService extends IService<Orders> {

    void submit(Orders orders, Long userId);

    Page<Orders> pageQuery(Long userId, Integer page, Integer pageSize);

    Page<Orders> adminPageQuery(Integer page, Integer pageSize, Long userId, Integer status);

    void updateStatus(Long id, Integer status);

    Orders getOrderDetail(Long id);

    void reminder(Long id);
}
