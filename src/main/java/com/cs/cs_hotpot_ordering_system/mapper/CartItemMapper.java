/**
 * 文件说明：购物车数据访问接口。
 * 作用：使用 MyBatis-Plus 完成购物车查询、添加、修改和删除。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 购物车表 Mapper。 */
@Mapper
public interface CartItemMapper extends BaseMapper<CartItem> {
    /** 查询购物车，并计算每条明细的小计金额。 */
    List<Map<String, Object>> findByUserId(@Param("userId") Long userId);

    /** 添加菜品，同一用户同一菜品只保留一条记录。 */
    int add(@Param("userId") Long userId,
            @Param("dishId") Long dishId,
            @Param("quantity") Integer quantity);

    /** 修改购物车记录。 */
    int updateQuantity(@Param("id") Long id,
                       @Param("userId") Long userId,
                       @Param("quantity") Integer quantity);

    /** 删除购物车记录。 */
    int deleteItem(@Param("id") Long id,
                   @Param("userId") Long userId);

    /** 订单提交成功后清空指定用户的购物车。 */
    int clearByUser(@Param("userId") Long userId);
}
