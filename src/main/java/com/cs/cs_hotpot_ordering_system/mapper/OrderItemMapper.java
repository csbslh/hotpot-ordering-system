/**
 * 文件说明：订单明细数据访问接口。
 * 作用：使用 MyBatis-Plus 保存订单中的菜品快照和数量。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/** 订单明细表 Mapper。 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}
