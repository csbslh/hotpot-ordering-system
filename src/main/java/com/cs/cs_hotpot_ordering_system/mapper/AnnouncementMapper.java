/**
 * 文件说明：公告数据访问接口。
 * 作用：使用 MyBatis-Plus 完成公告基础操作，并通过 XML 完成公告查询。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 公告表 Mapper。 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
    /** 查询已发布公告。 */
    List<Map<String, Object>> findPublished();

    /** 查询全部公告。 */
    List<Map<String, Object>> findAll();

    /** 新增并发布公告。 */
    int insertAnnouncement(@Param("title") String title,
                           @Param("content") String content,
                           @Param("publisherId") Long publisherId);

    /** 修改公告发布状态。 */
    int updateStatus(@Param("id") Long id,
                     @Param("publishStatus") Integer publishStatus);
}
