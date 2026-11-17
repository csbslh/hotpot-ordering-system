/**
 * 文件说明：浏览记录数据访问接口。
 * 作用：保存用户查看菜品的次数，并读取浏览记录供推荐系统使用。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.BrowseHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 浏览记录表 Mapper。 */
@Mapper
public interface BrowseHistoryMapper extends BaseMapper<BrowseHistory> {
    /** 查询用户浏览过的菜品及其点击次数。 */
    List<Map<String, Object>> findByUserId(@Param("userId") Long userId);

    /** 点击一次菜品，累加 browseCount。 */
    int record(@Param("userId") Long userId,
               @Param("dishId") Long dishId);

    /** 清空指定用户的浏览记录。 */
    int clear(@Param("userId") Long userId);
}
