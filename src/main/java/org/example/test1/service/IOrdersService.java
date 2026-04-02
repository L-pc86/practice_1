package org.example.test1.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Orders;

public interface IOrdersService extends IService<Orders> {

    void submit(Orders orders);

    Page<Orders> pageQuery(Long userId, Integer page, Integer pageSize);
}
