package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.test1.common.Result;
import org.example.test1.entity.AddressBook;
import org.example.test1.service.IAddressBookService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Tag(name = "地址簿管理", description = "收货地址相关接口")
public class AddressBookController {

    @Autowired
    private IAddressBookService addressBookService;

    @Operation(summary = "新增地址", description = "添加新的收货地址")
    @PostMapping
    public Result<String> save(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        addressBookService.saveAddress(addressBook, userId);
        return Result.success("新增地址成功");
    }

    @Operation(summary = "查询当前用户所有地址", description = "获取当前登录用户的所有收货地址")
    @GetMapping("/list")
    public Result<List<AddressBook>> list(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        List<AddressBook> list = addressBookService.listByUserId(userId);
        return Result.success(list);
    }

    @Operation(summary = "根据ID查询地址", description = "获取指定地址的详细信息")
    @GetMapping("/{id}")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @Operation(summary = "修改地址", description = "更新收货地址信息")
    @PutMapping
    public Result<String> update(@RequestBody AddressBook addressBook) {
        addressBookService.updateAddress(addressBook);
        return Result.success("修改地址成功");
    }

    @Operation(summary = "删除地址", description = "根据ID删除收货地址")
    @DeleteMapping
    public Result<String> delete(@RequestParam Long id) {
        addressBookService.deleteById(id);
        return Result.success("删除地址成功");
    }

    @Operation(summary = "设置默认地址", description = "将指定地址设为默认收货地址")
    @PutMapping("/default")
    public Result<String> setDefault(@RequestParam Long id, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        addressBookService.setDefault(id, userId);
        return Result.success("设置默认地址成功");
    }

    @Operation(summary = "查询默认地址", description = "获取当前用户的默认收货地址")
    @GetMapping("/default")
    public Result<AddressBook> getDefault(HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("userId");
        AddressBook addressBook = addressBookService.getDefault(userId);
        return Result.success(addressBook);
    }
}
