package org.example.test1.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.test1.common.Result;
import org.example.test1.entity.Org;
import org.example.test1.service.IOrgService;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/org")
@Tag(name = "组织管理", description = "组织相关接口")
public class OrgController {

    @Autowired
    private IOrgService orgService;

    @Operation(summary = "查询所有组织", description = "获取组织列表")
    @GetMapping("/list")
    public Result<List<Org>> list() {
        List<Org> orgList = orgService.listAll();
        return Result.success(orgList);
    }

    @Operation(summary = "根据父ID查询组织", description = "获取指定父组织的子组织列表")
    @GetMapping("/listByParent")
    public Result<List<Org>> listByParent(@RequestParam Integer parentId) {
        List<Org> orgList = orgService.listByParentId(parentId);
        return Result.success(orgList);
    }

    @Operation(summary = "根据ID查询组织", description = "获取单个组织信息")
    @GetMapping("/{id}")
    public Result<Org> getById(@PathVariable Integer id) {
        Org org = orgService.getById(id);
        return Result.success(org);
    }

    @Operation(summary = "创建组织", description = "新增组织")
    @PostMapping("/create")
    public Result<Boolean> create(@Valid @RequestBody Org org) {
        boolean isSave = orgService.create(org);
        return Result.success(isSave);
    }

    @Operation(summary = "更新组织", description = "修改组织信息")
    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody Org org) {
        boolean isUpdate = orgService.update(org);
        return Result.success(isUpdate);
    }

    @Operation(summary = "删除组织", description = "根据ID删除组织")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(@RequestParam Integer id) {
        boolean isDelete = orgService.delete(id);
        return Result.success(isDelete);
    }
}
