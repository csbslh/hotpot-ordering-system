/**
 * 文件说明：收藏菜品控制器。
 * 作用：处理普通用户收藏、取消收藏和查询收藏菜品的请求。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.service.FavoriteDishService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favoriteDishes")
public class FavoriteDishController {
    private final FavoriteDishService service;
    public FavoriteDishController(FavoriteDishService service) { this.service = service; }
    /** 查询用户收藏的全部菜品。 */
    @GetMapping public ApiResponse<?> list(@RequestParam("userId") Long userId) { return ApiResponse.success(service.list(userId)); }
    /** 判断用户是否收藏指定菜品。 */
    @GetMapping("/exists") public ApiResponse<?> exists(@RequestParam("userId") Long userId,@RequestParam("dishId") Long dishId) { return ApiResponse.success(service.exists(userId,dishId)); }
    /** 收藏一份菜品。 */
    @PostMapping public ApiResponse<?> add(@RequestParam("userId") Long userId,@RequestParam("dishId") Long dishId) { service.add(userId,dishId); return ApiResponse.success(null); }
    /** 取消收藏一份菜品。 */
    @DeleteMapping public ApiResponse<?> remove(@RequestParam("userId") Long userId,@RequestParam("dishId") Long dishId) { service.remove(userId,dishId); return ApiResponse.success(null); }
}
