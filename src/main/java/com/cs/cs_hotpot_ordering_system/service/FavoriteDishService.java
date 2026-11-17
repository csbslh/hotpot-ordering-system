/**
 * 文件说明：收藏菜品业务层。
 * 作用：处理用户收藏关系的查询、新增、删除和判断。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.mapper.FavoriteDishMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class FavoriteDishService {
    private final FavoriteDishMapper mapper;
    public FavoriteDishService(FavoriteDishMapper mapper) { this.mapper = mapper; }
    /** 查询用户收藏列表。 */
    public List<Map<String,Object>> list(Long userId) { return mapper.findByUserId(userId); }
    /** 收藏菜品。 */
    public void add(Long userId, Long dishId) { mapper.add(userId,dishId); }
    /** 取消收藏。 */
    public void remove(Long userId, Long dishId) { mapper.remove(userId,dishId); }
    /** 判断是否已经收藏。 */
    public boolean exists(Long userId, Long dishId) { return mapper.exists(userId,dishId); }
}
