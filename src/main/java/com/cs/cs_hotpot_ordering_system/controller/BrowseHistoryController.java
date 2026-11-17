/**
 * 文件说明：浏览记录控制器。
 * 作用：记录用户查看过的菜品，并为简单的点击次数推荐提供数据。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.service.BrowseHistoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/browseHistories")
public class BrowseHistoryController {
    private final BrowseHistoryService service;
    public BrowseHistoryController(BrowseHistoryService service) { this.service = service; }
    /** 查询用户的浏览记录。 */
    @GetMapping public ApiResponse<?> list(@RequestParam("userId") Long userId) { return ApiResponse.success(service.list(userId)); }
    /** 用户每点击一次菜品详情，就调用一次该接口，browseCount 会自动加1。 */
    @PostMapping public ApiResponse<?> record(@RequestParam("userId") Long userId,@RequestParam("dishId") Long dishId) { service.record(userId,dishId); return ApiResponse.success(null); }
    /** 清空用户的浏览记录。 */
    @DeleteMapping public ApiResponse<?> clear(@RequestParam("userId") Long userId) { service.clear(userId); return ApiResponse.success(null); }
}
