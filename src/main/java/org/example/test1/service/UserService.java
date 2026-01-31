package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.User;
import org.example.test1.mapper.UserMapper;
import org.springframework.stereotype.Service;


@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IUserService{
}
