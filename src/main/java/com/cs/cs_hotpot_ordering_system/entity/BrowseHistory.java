package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件说明：浏览记录实体类，对应 browseHistory 表。
 * 作用：统计"某个用户累计浏览某个菜品多少次、最近一次什么时候看的"，
 *       是个性化推荐（按浏览次数推荐）的数据来源。
 */
@Data
@TableName("browseHistory")
public class BrowseHistory {
    /** 浏览记录主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 浏览用户编号。 */
    private Long userId;
    /** 被浏览的菜品编号。 */
    private Long dishId;
    /** 累计浏览次数，每浏览一次加一。 */
    private Integer browseCount;
    /** 最近一次浏览时间。 */
    private LocalDateTime lastBrowsedAt;
}
