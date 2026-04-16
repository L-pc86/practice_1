package org.example.test1.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.test1.common.exception.BusinessException;
import org.example.test1.common.ResultCodeEnum;
import org.example.test1.entity.Org;
import org.example.test1.mapper.OrgMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrgService extends ServiceImpl<OrgMapper, Org> implements IOrgService {

    @Override
    public List<Org> listAll() {
        return list();
    }

    @Override
    public Org getOrgById(Integer id) {
        Org org = super.getById(id);
        if (org == null) {
            throw new BusinessException(ResultCodeEnum.ERROR, "组织不存在");
        }
        return org;
    }

    @Override
    public boolean create(Org org) {
        return save(org);
    }

    @Override
    public boolean update(Org org) {
        return updateById(org);
    }

    @Override
    public boolean delete(Integer id) {
        LambdaQueryWrapper<Org> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Org::getParentId, id);
        long count = count(wrapper);
        if (count > 0) {
            throw new BusinessException(ResultCodeEnum.ERROR, "该组织下存在子组织，无法删除");
        }
        return removeById(id);
    }

    @Override
    public List<Org> listByParentId(Integer parentId) {
        LambdaQueryWrapper<Org> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Org::getParentId, parentId);
        return list(wrapper);
    }
}
