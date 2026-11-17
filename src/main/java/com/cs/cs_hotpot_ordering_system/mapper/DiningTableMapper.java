/**
 * 文件说明：桌台数据访问接口。
 * 作用：使用 MyBatis-Plus 读取桌台列表，并修改桌台状态。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.DiningTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 桌台表 Mapper。 */
@Mapper
public interface DiningTableMapper extends BaseMapper<DiningTable> {
    /** 查询全部桌台。 */
    List<DiningTable> findAll();

    /** 修改桌台的使用状态。 */
    int updateStatus(@Param("id") Long id,
                     @Param("tableStatus") String tableStatus);
}
