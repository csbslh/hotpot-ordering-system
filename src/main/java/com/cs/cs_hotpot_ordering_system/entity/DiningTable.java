/**
 * 文件说明：桌台实体类，对应 diningTable 表。
 * 作用：记录桌号、座位数量和当前用餐状态。
 */
package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("diningTable")
public class DiningTable {
    /** 桌台主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 桌台编号。 */
    private String tableNumber;
    /** 可容纳人数。 */
    private Integer seatNumber;
    /** 桌台状态：AVAILABLE 空闲、OCCUPIED 用餐中、RESERVED 已预订。 */
    private String tableStatus;
    /** 创建时间。 */
    private LocalDateTime createdAt;
    /** 最后更新时间。 */
    private LocalDateTime updatedAt;
}
