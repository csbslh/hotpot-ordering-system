/**
 * 文件说明：评价管理控制器。
 * 作用：普通用户提交评价，管理员查询评价并进行回复。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService service;
    public ReviewController(ReviewService service) { this.service = service; }
    /** 查询全部评价或查询某个用户自己的评价。 */
    @GetMapping public ApiResponse<?> list(@RequestParam(value="userId", required=false) Long userId) { return ApiResponse.success(userId == null ? service.all() : service.mine(userId)); }
    /** 用户提交一次订单整体用餐评价。 */
    @PostMapping public ApiResponse<?> add(@RequestBody Map<String,Object> body) { service.add(longValue(body,"orderId"),longValue(body,"userId"),intValue(body,"rating"),String.valueOf(body.getOrDefault("content",""))); return ApiResponse.success(null); }
    /** 管理员回复用户评价。 */
    @PutMapping("/{id}/reply") public ApiResponse<?> reply(@PathVariable("id") Long id,@RequestBody Map<String,Object> body) { service.reply(id,String.valueOf(body.getOrDefault("replyContent",""))); return ApiResponse.success(null); }
    /** 用户删除自己的评价。 */
    @DeleteMapping("/{id}") public ApiResponse<?> delete(@PathVariable("id") Long id,@RequestParam("userId") Long userId) { service.delete(id,userId); return ApiResponse.success(null); }
    /** 用户回复管理员。 */
    @PutMapping("/{id}/user-reply") public ApiResponse<?> userReply(@PathVariable("id") Long id,@RequestBody Map<String,Object> body) { service.userReply(id,longValue(body,"userId"),String.valueOf(body.getOrDefault("userReplyContent",""))); return ApiResponse.success(null); }
    private static Long longValue(Map<String,Object> body,String key) { Object value=body.get(key); return value == null ? null : Long.valueOf(value.toString()); }
    private static Integer intValue(Map<String,Object> body,String key) { Object value=body.get(key); return value == null ? null : Integer.valueOf(value.toString()); }
}
