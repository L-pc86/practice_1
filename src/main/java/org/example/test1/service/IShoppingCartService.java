package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.ShoppingCart;
import java.util.List;

public interface IShoppingCartService extends IService<ShoppingCart> {

    ShoppingCart addToCart(ShoppingCart shoppingCart, Long userId);

    ShoppingCart subFromCart(Long userId, Long dishId, Long setmealId, String dishFlavor);

    List<ShoppingCart> listByUserId(Long userId);

    void cleanCart(Long userId);
}
