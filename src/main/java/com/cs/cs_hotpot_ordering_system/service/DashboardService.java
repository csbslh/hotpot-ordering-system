/**
 * 文件说明：管理员工作台业务层。
 * 作用：组合多个统计查询结果，形成前端首页所需的概览数据。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.mapper.DashboardMapper;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DashboardService {
    private final DashboardMapper dashboardMapper;
    public DashboardService(DashboardMapper dashboardMapper) { this.dashboardMapper = dashboardMapper; }
    /** 组合营业概况、最新订单、热销菜品和桌台统计。 */
    public Map<String,Object> overview() {
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("summary", dashboardMapper.summary()); result.put("latestOrders", dashboardMapper.latestOrders(10));
        result.put("popularDishes", dashboardMapper.popularDishes(10)); result.put("tableSummary", dashboardMapper.tableSummary()); result.put("dailyTrend", dashboardMapper.dailyTrend()); return result;
    }
}
