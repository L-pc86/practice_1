package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.ShoppingCart;
import org.example.test1.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShoppingCartService extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Override
    public ShoppingCart addToCart(ShoppingCart shoppingCart, Long userId) {
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();

        if (dishId != null) {
            wrapper.eq(ShoppingCart::getDishId, dishId);
            if (shoppingCart.getDishFlavor() != null) {
                wrapper.eq(ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());
            }
        } else if (setmealId != null) {
            wrapper.eq(ShoppingCart::getSetmealId, setmealId);
        } else {
            throw new BusinessException(ResultCodeEnum.PARAM_ERROR, "参数错误");
        }

        ShoppingCart cartItem = getOne(wrapper);
        if (cartItem != null) {
            cartItem.setNumber(cartItem.getNumber() + 1);
            updateById(cartItem);
            return cartItem;
        } else {
            shoppingCart.setNumber(1);
            save(shoppingCart);
            return shoppingCart;
        }
    }

    @Override
    public ShoppingCart subFromCart(Long userId, Long dishId, Long setmealId, String dishFlavor) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);

        if (dishId != null) {
            wrapper.eq(ShoppingCart::getDishId, dishId);
            if (dishFlavor != null) {
                wrapper.eq(ShoppingCart::getDishFlavor, dishFlavor);
            }
        } else if (setmealId != null) {
            wrapper.eq(ShoppingCart::getSetmealId, setmealId);
        }

        ShoppingCart cartItem = getOne(wrapper);
        if (cartItem == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "购物车中没有该商品");
        }

        if (cartItem.getNumber() > 1) {
            cartItem.setNumber(cartItem.getNumber() - 1);
            updateById(cartItem);
            return cartItem;
        } else {
            removeById(cartItem.getId());
            return null;
        }
    }

    @Override
    public List<ShoppingCart> listByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        return list(wrapper);
    }

    @Override
    public void cleanCart(Long userId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        remove(wrapper);
    }
}
