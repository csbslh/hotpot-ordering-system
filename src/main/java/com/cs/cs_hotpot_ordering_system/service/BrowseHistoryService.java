/**
 * 文件说明：浏览记录业务层。
 * 作用：管理用户菜品浏览记录，为点击次数推荐提供简单的数据基础。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.mapper.BrowseHistoryMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class BrowseHistoryService {
    private final BrowseHistoryMapper mapper;
    public BrowseHistoryService(BrowseHistoryMapper mapper) { this.mapper = mapper; }
    /** 查询用户浏览记录。 */
    public List<Map<String,Object>> list(Long userId) { return mapper.findByUserId(userId); }
    /** 记录一次菜品点击。 */
    public void record(Long userId, Long dishId) { mapper.record(userId,dishId); }
    /** 清空用户浏览记录。 */
    public void clear(Long userId) { mapper.clear(userId); }
}
