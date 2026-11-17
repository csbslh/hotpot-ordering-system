/**
 * 文件说明：菜品分类实体类，对应 dishCategory 表。
 * 作用：对火锅菜品进行分类，便于用户浏览和管理员维护。
 */
package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("dishCategory")
public class DishCategory {
    /** 分类主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 分类名称。 */
    private String categoryName;
    /** 排序号，数值越小越靠前。 */
    private Integer sortNumber;
    /** 分类状态：1 启用，0 停用。 */
    private Integer status;
    /** 创建时间。 */
    private LocalDateTime createdAt;
    /** 最后更新时间。 */
    private LocalDateTime updatedAt;
}
