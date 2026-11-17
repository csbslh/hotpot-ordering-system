package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件说明：评价实体类，对应 dishReview 表。
 * 作用：保存用户对菜品的评分和评价内容，以及管理员的回复。
 */
@Data
@TableName("dishReview")
public class DishReview {
    /** 评价主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 关联的订单编号，用于判断用户是否真实消费过。 */
    private Long orderId;
    /** 评价用户编号，关联 user 表。 */
    private Long userId;
    /** 被评价的菜品编号，关联 dish 表。 */
    private Long dishId;
    /** 评分，1~5 星。 */
    private Integer rating;
    /** 评价正文内容。 */
    private String content;
    /** 管理员对评价的回复内容。 */
    private String replyContent;
    /** 用户对管理员回复的再次回复。 */
    private String userReplyContent;
    /** 评价状态（如是否审核、是否显示）。 */
    private Integer reviewStatus;
    /** 评价创建时间。 */
    private LocalDateTime createdAt;
}
