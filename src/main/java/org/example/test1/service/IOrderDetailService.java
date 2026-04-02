package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.OrderDetail;
import java.util.List;

public interface IOrderDetailService extends IService<OrderDetail> {

    List<OrderDetail> listByOrderId(Long orderId);

    void saveBatch(List<OrderDetail> orderDetails);

    void deleteByOrderId(Long orderId);

    List<OrderDetail> listByOrderIds(List<Long> orderIds);
}
