/**
 * 文件说明：菜品推荐业务层。
 * 作用：限制推荐条数，并调用 Mapper 按用户点击次数生成推荐结果。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.Dish;
import com.cs.cs_hotpot_ordering_system.mapper.DishMapper;
import com.cs.cs_hotpot_ordering_system.mapper.RecommendationMapper;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {
    private final RecommendationMapper recommendationMapper;
    private final DishMapper dishMapper;

    public RecommendationService(RecommendationMapper recommendationMapper, DishMapper dishMapper) {
        this.recommendationMapper = recommendationMapper;
        this.dishMapper = dishMapper;
    }

    /** 推荐数量限制在 1 到 20 条，优先按点击次数推荐。 */
    public List<Map<String,Object>> recommend(Long userId, Integer limit) {
        int size = limit == null ? 6 : Math.min(Math.max(limit, 1), 20);
        try {
            return recommendationMapper.findForUser(userId == null ? -1L : userId, size);
        } catch (Exception exception) {
            // 旧数据库尚未创建 browseHistory 时，按销量返回兜底数据，避免首页推荐接口报错。
            List<Dish> dishes = dishMapper.findList(null, null, null);
            List<Map<String,Object>> result = new ArrayList<>();
            dishes.stream().limit(size).forEach(dish -> {
                Map<String,Object> item = new LinkedHashMap<>();
                item.put("id", dish.getId());
                item.put("dishName", dish.getDishName());
                item.put("description", dish.getDescription());
                item.put("coverImage", dish.getCoverImage());
                item.put("price", dish.getPrice());
                item.put("clickCount", 0);
                item.put("recommendationReason", "按销量推荐");
                result.add(item);
            });
            return result;
        }
    }
}