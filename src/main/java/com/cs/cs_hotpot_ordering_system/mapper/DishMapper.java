/**
 * 文件说明：菜品数据访问接口。
 * 作用：使用 MyBatis-Plus 根据条件查询菜品，并完成菜品修改和下架。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cs.cs_hotpot_ordering_system.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜品表 Mapper。
 * 通过 BaseMapper 和 QueryWrapper 实现纯 MyBatis-Plus 数据访问。
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
    /** 动态查询上架菜品。 */
    default List<Dish> findList(Long categoryId, String keyword, Boolean recommended) {
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (categoryId != null) {
            wrapper.eq("categoryId", categoryId);
        }
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like("dishName", keyword.trim());
        }
        if (Boolean.TRUE.equals(recommended)) {
            wrapper.eq("isRecommended", 1);
        }
        wrapper.orderByDesc("isRecommended")
                .orderByDesc("saleNumber")
                .orderByDesc("createdAt");
        return selectList(wrapper);
    }

    /** 查询一份上架菜品。 */
    default Dish findById(Long id) {
        return selectOne(new QueryWrapper<Dish>()
                .eq("id", id)
                .eq("status", 1));
    }

    /** 修改菜品基本信息。 */
    default int updateDish(Long id, Dish dish) {
        dish.setId(id);
        return updateById(dish);
    }

    /** 逻辑下架菜品。 */
    default int deleteDish(Long id) {
        Dish dish = new Dish();
        dish.setId(id);
        dish.setStatus(0);
        return updateById(dish);
    }
    /**
     * 累加菜品销量。
     * 订单确认时由订单业务层调用，使用数据库自增表达式避免并发下覆盖其他订单的销量。
     */
    default int increaseSaleNumber(Long id, Integer quantity) {
        if (id == null || quantity == null || quantity <= 0) {
            return 0;
        }
        UpdateWrapper<Dish> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", id)
                .setSql("saleNumber = COALESCE(saleNumber, 0) + " + quantity);
        return update(null, wrapper);
    }
}
