package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.AddressBook;
import org.example.test1.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AddressBookService extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Override
    public void saveAddress(AddressBook addressBook, Long userId) {
        addressBook.setUserId(userId);
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        Long count = count(wrapper);
        if (count == 0) {
            addressBook.setIsDefault(1);
        }
        save(addressBook);
    }

    @Override
    public List<AddressBook> listByUserId(Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.orderByDesc(AddressBook::getIsDefault);
        wrapper.orderByDesc(AddressBook::getUpdateTime);
        return list(wrapper);
    }

    @Override
    public AddressBook getById(Long id) {
        return getById(id);
    }

    @Override
    public void updateAddress(AddressBook addressBook) {
        updateById(addressBook);
    }

    @Override
    public void deleteById(Long id) {
        removeById(id);
    }

    @Override
    public void setDefault(Long id, Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> list = list(wrapper);
        for (AddressBook ab : list) {
            ab.setIsDefault(0);
            updateById(ab);
        }
        AddressBook addressBook = new AddressBook();
        addressBook.setId(id);
        addressBook.setIsDefault(1);
        updateById(addressBook);
    }

    @Override
    public AddressBook getDefault(Long userId) {
        LambdaQueryWrapper<AddressBook> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AddressBook::getUserId, userId);
        wrapper.eq(AddressBook::getIsDefault, 1);
        return getOne(wrapper);
    }
}
