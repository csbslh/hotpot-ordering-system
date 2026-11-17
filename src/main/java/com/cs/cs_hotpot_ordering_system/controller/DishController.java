/**
 * 文件说明：菜品控制器。
 * 作用：处理菜品浏览、搜索、详情、后台维护和个性化推荐请求。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.entity.Dish;
import com.cs.cs_hotpot_ordering_system.service.DishService;
import com.cs.cs_hotpot_ordering_system.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
    private final DishService dishService;
    private final RecommendationService recommendationService;
    public DishController(DishService dishService, RecommendationService recommendationService) { this.dishService = dishService; this.recommendationService = recommendationService; }
    /** 按分类、关键字和推荐标记查询菜品。 */
    @GetMapping public ApiResponse<?> list(@RequestParam(value="categoryId", required=false) Long categoryId, @RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="recommended", required=false) Boolean recommended) { return ApiResponse.success(dishService.list(categoryId,keyword,recommended)); }
    /** 查询菜品详情；前端展示详情时可同时记录一次浏览。 */
    @GetMapping("/{id}") public ApiResponse<?> detail(@PathVariable("id") Long id) { return ApiResponse.success(dishService.detail(id)); }
    /** 根据用户点击次数返回猜你喜欢菜品。 */
    @GetMapping("/recommendations") public ApiResponse<?> recommendations(@RequestParam(value="userId", required=false) Long userId, @RequestParam(value="limit", required=false) Integer limit) { return ApiResponse.success(recommendationService.recommend(userId,limit)); }
    /** 管理员新增菜品。 */
    @PostMapping public ApiResponse<?> add(@RequestBody Dish dish) { return ApiResponse.success("新增成功", dishService.add(dish)); }
    /** 管理员修改菜品信息。 */
    @PutMapping("/{id}") public ApiResponse<?> update(@PathVariable("id") Long id, @RequestBody Dish dish) { dishService.update(id,dish); return ApiResponse.success(null); }
    /** 逻辑下架菜品。 */
    @DeleteMapping("/{id}") public ApiResponse<?> remove(@PathVariable("id") Long id) { dishService.remove(id); return ApiResponse.success(null); }
}
