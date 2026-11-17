package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 文件说明：订单实体类，对应数据库中的 orders 表。
 * 作用：保存一次点餐的整体信息（谁、哪桌、多少钱、什么状态）。
 */
@Data
@TableName("orders")
public class HotPotOrder {
    /** 订单主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 订单编号，提交订单时按 "HP+时间戳" 规则生成，方便展示和对账。 */
    private String orderNumber;
    /** 下单用户编号，关联 user 表。 */
    private Long userId;
    /** 用餐桌台编号，关联 diningTable 表。 */
    private Long tableId;
    /** 订单总金额，由购物车各菜品小计累加得到。 */
    private BigDecimal totalAmount;
    /** 订单状态：PENDING 待确认、CONFIRMED 已确认、COOKING 制作中、COMPLETED 已完成、CANCELLED 已取消。 */
    private String orderStatus;
    /** 用户下单时填写的备注（如口味、忌口）。 */
    private String remark;
    /** 下单时间。 */
    private LocalDateTime orderedAt;
    /** 订单完成时间。 */
    private LocalDateTime completedAt;
}
