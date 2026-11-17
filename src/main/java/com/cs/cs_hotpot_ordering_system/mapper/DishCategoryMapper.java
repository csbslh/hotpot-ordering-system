/**
 * 文件说明：菜品分类数据访问接口。
 * 作用：使用 MyBatis-Plus 执行分类查询、修改和逻辑删除。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.DishCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 菜品分类表 Mapper。 */
@Mapper
public interface DishCategoryMapper extends BaseMapper<DishCategory> {
    /** 查询启用的分类。 */
    List<DishCategory> findAll();

    /** 修改分类名称和排序号。 */
    int updateCategory(@Param("id") Long id,
                       @Param("category") DishCategory category);

    /** 逻辑停用分类。 */
    int deleteCategory(@Param("id") Long id);
}
