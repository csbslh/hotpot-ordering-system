package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件说明：公告实体类，对应 announcement 表。
 * 作用：保存管理员发布的店铺公告（如新品上架、优惠活动），
 *       前台首页可以展示已发布的公告。
 */
@Data
@TableName("announcement")
public class Announcement {
    /** 公告主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 公告标题。 */
    private String title;
    /** 公告正文内容。 */
    private String content;
    /** 发布人（管理员）的用户编号。 */
    private Long publisherId;
    /** 发布状态：1 已发布，0 未发布（草稿）。 */
    private Integer publishStatus;
    /** 发布时间。 */
    private LocalDateTime publishedAt;
    /** 创建时间。 */
    private LocalDateTime createdAt;
}
