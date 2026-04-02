package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.entity.AddressBook;
import org.example.test1.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

@Service
public class AddressBookService extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {
}
