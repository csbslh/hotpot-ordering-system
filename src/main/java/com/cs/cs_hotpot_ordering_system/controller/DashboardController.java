/**
 * 文件说明：管理员工作台控制器。
 * 作用：返回营业额、订单、热销菜品和桌台状态等首页统计数据。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.service.DashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService) { this.dashboardService = dashboardService; }
    /** 查询管理首页所需的全部概览数据。 */
    @GetMapping("/overview") public ApiResponse<?> overview() { return ApiResponse.success(dashboardService.overview()); }
}
