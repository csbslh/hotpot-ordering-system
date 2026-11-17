package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件说明：购物车条目实体类，对应 cartItem 表。
 * 作用：记录某个用户购物车中加了哪个菜品、加了几份，提交订单后会清空。
 */
@Data
@TableName("cartItem")
public class CartItem {
    /** 购物车条目主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属用户编号，每个用户独立维护一份购物车。 */
    private Long userId;
    /** 菜品编号，关联 dish 表。 */
    private Long dishId;
    /** 该菜品加入的数量。 */
    private Integer quantity;
    /** 加入购物车的时间。 */
    private LocalDateTime createdAt;
    /** 最后更新时间。 */
    private LocalDateTime updatedAt;
}
