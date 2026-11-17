/**
 * 文件说明：管理员工作台数据访问接口。
 * 作用：统计今日营业数据、最新订单、热销菜品和桌台状态。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 工作台统计 Mapper，不对应单一实体表。 */
@Mapper
public interface DashboardMapper {
    /** 查询今日营业额、订单数和菜品销售数量。 */
    Map<String, Object> summary();

    /** 查询最近提交的订单。 */
    List<Map<String, Object>> latestOrders(@Param("limit") Integer limit);

    /** 按销量倒序查询热销菜品。 */
    List<Map<String, Object>> popularDishes(@Param("limit") Integer limit);

    /** 查询最近七天每天的营业额。 */
    List<Map<String, Object>> dailyTrend();

    /** 按状态统计桌台数量。 */
    List<Map<String, Object>> tableSummary();
}
