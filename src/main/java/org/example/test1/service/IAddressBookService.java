package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.AddressBook;
import java.util.List;

public interface IAddressBookService extends IService<AddressBook> {

    void saveAddress(AddressBook addressBook, Long userId);

    List<AddressBook> listByUserId(Long userId);

    AddressBook getAddressById(Long id);

    void updateAddress(AddressBook addressBook);

    void deleteById(Long id);

    void setDefault(Long id, Long userId);

    AddressBook getDefault(Long userId);
}
