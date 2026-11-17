/**
 * 文件说明：菜品分类业务层。
 * 作用：处理分类名称校验及分类维护业务。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.DishCategory;
import com.cs.cs_hotpot_ordering_system.mapper.DishCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishCategoryService {
    private final DishCategoryMapper mapper;

    public DishCategoryService(DishCategoryMapper mapper) {
        this.mapper = mapper;
    }

    /** 查询启用的分类。 */
    public List<DishCategory> list() {
        return mapper.findAll();
    }

    /** 校验分类名称后新增分类。 */
    public void add(DishCategory category) {
        if (category == null || category.getCategoryName() == null
                || category.getCategoryName().isBlank()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        if (mapper.insert(category) != 1) {
            throw new IllegalStateException("分类保存失败");
        }
    }

    /** 修改分类信息。 */
    public void update(Long id, DishCategory category) {
        if (mapper.updateCategory(id, category) == 0) {
            throw new IllegalArgumentException("分类不存在");
        }
    }

    /** 停用分类。 */
    public void remove(Long id) {
        if (mapper.deleteCategory(id) == 0) {
            throw new IllegalArgumentException("分类不存在");
        }
    }
}
