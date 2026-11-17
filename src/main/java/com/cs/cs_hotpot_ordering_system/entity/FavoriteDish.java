package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件说明：收藏实体类，对应 favoriteDish 表。
 * 作用：记录"哪个用户收藏了哪个菜品"的多对多关系，同一用户同一菜品只存一条。
 */
@Data
@TableName("favoriteDish")
public class FavoriteDish {
    /** 收藏记录主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 收藏该菜品的用户编号。 */
    private Long userId;
    /** 被收藏的菜品编号。 */
    private Long dishId;
    /** 收藏时间。 */
    private LocalDateTime createdAt;
}
