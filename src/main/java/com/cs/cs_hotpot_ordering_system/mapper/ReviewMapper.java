/**
 * 文件说明：评价数据访问接口。
 * 作用：使用 MyBatis-Plus 查询评价、保存评价内容和管理员回复。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cs.cs_hotpot_ordering_system.entity.DishReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 用餐评价表 Mapper。 */
@Mapper
public interface ReviewMapper extends BaseMapper<DishReview> {
    /** 查询全部评价。 */
    List<Map<String, Object>> findAll();

    /** 查询指定用户提交过的评价。 */
    List<Map<String, Object>> findByUser(@Param("userId") Long userId);

    /** 判断用户是否已经评价过该订单。 */
    default boolean existsByOrderAndUser(Long orderId, Long userId) {
        return selectCount(new QueryWrapper<DishReview>()
                .eq("orderId", orderId)
                .eq("userId", userId)) > 0;
    }

    /** 保存一条整体用餐评价。 */
    int insertReview(@Param("orderId") Long orderId,
                     @Param("userId") Long userId,
                     @Param("rating") Integer rating,
                     @Param("content") String content);

    /** 保存管理员对评价的回复。 */
    int reply(@Param("id") Long id,
              @Param("replyContent") String replyContent);

    /** 删除指定用户自己的评价。 */
    int deleteByIdAndUser(@Param("id") Long id,
                          @Param("userId") Long userId);

    /** 保存用户对管理员回复的追评。 */
    int userReply(@Param("id") Long id,
                  @Param("userId") Long userId,
                  @Param("userReplyContent") String userReplyContent);
}
