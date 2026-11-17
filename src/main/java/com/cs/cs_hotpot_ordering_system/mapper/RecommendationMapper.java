/**
 * 文件说明：简单推荐数据访问接口。
 * 作用：按照用户浏览记录中的点击次数排序，点击次数越多的菜品越优先推荐。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 推荐查询 Mapper。 */
@Mapper
public interface RecommendationMapper {
    /** 基于当前用户的点击次数生成推荐。 */
    List<Map<String, Object>> findForUser(@Param("userId") Long userId,
                                          @Param("limit") Integer limit);
}
