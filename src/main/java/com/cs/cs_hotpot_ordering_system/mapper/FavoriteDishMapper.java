/**
 * 文件说明：收藏菜品数据访问接口。
 * 作用：使用 MyBatis-Plus 保存用户收藏关系，并查询收藏菜品详情。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.FavoriteDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 收藏关系表 Mapper。 */
@Mapper
public interface FavoriteDishMapper extends BaseMapper<FavoriteDish> {
    /** 查询用户收藏的菜品列表。 */
    List<Map<String, Object>> findByUserId(@Param("userId") Long userId);

    /** 新增收藏。 */
    int add(@Param("userId") Long userId,
            @Param("dishId") Long dishId);

    /** 删除收藏关系。 */
    int remove(@Param("userId") Long userId,
               @Param("dishId") Long dishId);

    /** 判断指定菜品是否已经被用户收藏。 */
    boolean exists(@Param("userId") Long userId,
                   @Param("dishId") Long dishId);
}
