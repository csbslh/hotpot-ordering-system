/**
 * 文件说明：订单数据访问接口。
 * 作用：使用 MyBatis-Plus 完成订单查询、购物车读取和状态修改。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.HotPotOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 订单表 Mapper。 */
@Mapper
public interface OrderMapper extends BaseMapper<HotPotOrder> {
    /** 查询指定用户的订单。 */
    List<Map<String, Object>> findByUserId(@Param("userId") Long userId);

    /** 查询全部订单。 */
    List<Map<String, Object>> findAll();

    /** 查询订单明细。 */
    List<Map<String, Object>> findItems(@Param("orderId") Long orderId);

    /** 查询购物车内容，用于生成订单。 */
    List<Map<String, Object>> findCartItems(@Param("userId") Long userId);

    /** 修改订单状态。 */
    int updateStatus(@Param("id") Long id,
                     @Param("orderStatus") String orderStatus);
}
