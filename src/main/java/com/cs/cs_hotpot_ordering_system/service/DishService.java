/**
 * 文件说明：菜品业务层。
 * 作用：处理菜品查询、搜索、详情和管理员维护，并校验关键字段。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.Dish;
import com.cs.cs_hotpot_ordering_system.mapper.DishMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishMapper dishMapper;

    public DishService(DishMapper dishMapper) {
        this.dishMapper = dishMapper;
    }

    /** 根据条件查询菜品。 */
    public List<Dish> list(Long categoryId, String keyword, Boolean recommended) {
        return dishMapper.findList(categoryId, keyword, recommended);
    }

    /** 查询菜品详情，不存在时抛出业务异常。 */
    public Dish detail(Long id) {
        Dish dish = dishMapper.findById(id);
        if (dish == null) {
            throw new IllegalArgumentException("菜品不存在");
        }
        return dish;
    }

    /** 校验菜品后新增，并返回菜品主键。 */
    public Long add(Dish dish) {
        validate(dish);
        if (dishMapper.insert(dish) != 1 || dish.getId() == null) {
            throw new IllegalStateException("菜品保存失败");
        }
        return dish.getId();
    }

    /** 校验菜品后修改。 */
    public void update(Long id, Dish dish) {
        validate(dish);
        if (dishMapper.updateDish(id, dish) == 0) {
            throw new IllegalArgumentException("菜品不存在");
        }
    }

    /** 逻辑下架菜品。 */
    public void remove(Long id) {
        if (dishMapper.deleteDish(id) == 0) {
            throw new IllegalArgumentException("菜品不存在");
        }
    }

    /** 检查新增或修改菜品时的必填项。 */
    private void validate(Dish dish) {
        if (dish == null
                || dish.getCategoryId() == null
                || dish.getDishName() == null
                || dish.getDishName().isBlank()
                || dish.getPrice() == null) {
            throw new IllegalArgumentException("分类、菜品名称和价格不能为空");
        }
    }
}
