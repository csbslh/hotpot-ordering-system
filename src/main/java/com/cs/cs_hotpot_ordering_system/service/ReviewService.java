/**
 * 文件说明：评价业务层。
 * 作用：处理评价查询、评分校验、评价新增和管理员回复。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.HotPotOrder;
import com.cs.cs_hotpot_ordering_system.mapper.OrderMapper;
import com.cs.cs_hotpot_ordering_system.mapper.ReviewMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReviewService {
    private final ReviewMapper mapper;

    private final OrderMapper orderMapper;

    public ReviewService(ReviewMapper mapper, OrderMapper orderMapper) {
        this.mapper = mapper;
        this.orderMapper = orderMapper;
    }

    /** 查询全部评价。 */
    public List<Map<String, Object>> all() {
        return mapper.findAll();
    }

    /** 查询指定用户的评价。 */
    public List<Map<String, Object>> mine(Long userId) {
        return mapper.findByUser(userId);
    }

    /** 校验订单归属、完成状态和评分后保存一次整体用餐评价。 */
    public void add(Long orderId, Long userId, Integer rating, String content) {
        if (orderId == null || userId == null) {
            throw new IllegalArgumentException("订单和用户信息不能为空");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("评分必须为1至5星");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("请填写本次用餐感受");
        }

        HotPotOrder order = orderMapper.selectById(orderId);
        if (order == null || !userId.equals(order.getUserId())) {
            throw new IllegalArgumentException("订单不存在或不属于当前用户");
        }
        if (!"COMPLETED".equalsIgnoreCase(order.getOrderStatus())) {
            throw new IllegalArgumentException("订单完成后才可以评价");
        }
        if (mapper.existsByOrderAndUser(orderId, userId)) {
            throw new IllegalArgumentException("该订单已经评价过了");
        }
        if (mapper.insertReview(orderId, userId, rating, content) != 1) {
            throw new IllegalStateException("评价保存失败");
        }
    }

    /** 保存管理员回复。 */
    public void reply(Long id, String content) {
        if (mapper.reply(id, content) == 0) {
            throw new IllegalArgumentException("评价不存在");
        }
    }
    /** 用户删除自己的评价。 */
    public void delete(Long id, Long userId) {
        if (id == null || userId == null || mapper.deleteByIdAndUser(id, userId) == 0) {
            throw new IllegalArgumentException("评价不存在或无权删除");
        }
    }

    /** 用户回复管理员的评价回复。 */
    public void userReply(Long id, Long userId, String content) {
        if (id == null || userId == null || content == null || content.isBlank()) {
            throw new IllegalArgumentException("回复内容不能为空");
        }
        if (mapper.userReply(id, userId, content.trim()) == 0) {
            throw new IllegalArgumentException("评价不存在、不是本人评价或店家尚未回复");
        }
    }
}
