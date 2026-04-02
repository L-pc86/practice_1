package org.example.test1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.test1.entity.Org;
import java.util.List;

public interface IOrgService extends IService<Org> {

    List<Org> listAll();

    Org getById(Integer id);

    boolean create(Org org);

    boolean update(Org org);

    boolean delete(Integer id);

    List<Org> listByParentId(Integer parentId);
}
