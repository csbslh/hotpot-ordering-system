package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 文件说明：订单明细实体类，对应 orderItem 表。
 * 作用：保存订单中每一道菜的"快照"信息（下单那一刻的名称和价格），
 *       之后即使菜品改价或改名，历史订单依然显示当时的真实价格。
 */
@Data
@TableName("orderItem")
public class OrderItem {
    /** 订单明细主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属订单编号，关联 orders 表。 */
    private Long orderId;
    /** 菜品编号，关联 dish 表。 */
    private Long dishId;
    /** 下单时的菜品名称（快照）。 */
    private String dishName;
    /** 下单时的菜品单价（快照）。 */
    private BigDecimal unitPrice;
    /** 购买数量。 */
    private Integer quantity;
    /** 小计金额 = 单价 × 数量。 */
    private BigDecimal subtotal;
}
