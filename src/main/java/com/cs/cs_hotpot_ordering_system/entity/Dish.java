/**
 * 文件说明：菜品实体类，对应 dish 表。
 * 作用：保存菜品名称、价格、图片、销量和推荐状态等信息。
 */
package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dish")
public class Dish {
    /** 菜品主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 所属分类编号。 */
    private Long categoryId;
    /** 菜品名称。 */
    private String dishName;
    /** 菜品简介。 */
    private String description;
    /** 菜品封面图片地址。 */
    private String coverImage;
    /** 菜品单价。 */
    private BigDecimal price;
    /** 累计销售数量。 */
    private Integer saleNumber;
    /** 是否为管理员推荐：1 是，0 否。 */
    private Integer isRecommended;
    /** 是否为新品：1 是，0 否。 */
    private Integer isNew;
    /** 菜品状态：1 上架，0 下架。 */
    private Integer status;
    /** 创建时间。 */
    private LocalDateTime createdAt;
    /** 最后更新时间。 */
    private LocalDateTime updatedAt;
}
