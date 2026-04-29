package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.User;
import org.example.test1.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User wxLogin(String phone) {
        User user = lambdaQuery().eq(User::getPhone, phone).one();
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            save(user);
        }
        return user;
    }

    @Override
    public User getLoginUser(Long userId) {
        if (userId == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "用户未登录");
        }
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "用户不存在");
        }
        return user;
    }

    @Override
    public void updateUserInfo(User user, Long userId) {
        User existUser = getById(userId);
        if (existUser == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "用户不存在");
        }
        user.setId(userId);
        updateById(user);
    }
}
