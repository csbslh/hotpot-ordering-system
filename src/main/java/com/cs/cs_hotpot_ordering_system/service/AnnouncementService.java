/**
 * 文件说明：公告业务层。
 * 作用：在控制器和 Mapper 之间处理公告发布、查询和状态修改业务。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.Announcement;
import com.cs.cs_hotpot_ordering_system.mapper.AnnouncementMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnnouncementService {
    private final AnnouncementMapper mapper;

    public AnnouncementService(AnnouncementMapper mapper) {
        this.mapper = mapper;
    }

    /** 获取面向普通用户的已发布公告。 */
    public List<Map<String, Object>> published() {
        return mapper.findPublished();
    }

    /** 获取管理员可维护的全部公告。 */
    public List<Map<String, Object>> all() {
        return mapper.findAll();
    }

    /** 校验公告内容后保存公告。 */
    public void add(String title, String content, Long publisherId) {
        if (title == null || title.isBlank()
                || content == null || content.isBlank()) {
            throw new IllegalArgumentException("公告标题和内容不能为空");
        }
        if (mapper.insertAnnouncement(title, content, publisherId) != 1) {
            throw new IllegalStateException("公告保存失败");
        }
    }

    /** 修改公告标题和正文。 */
    public void update(Long id, String title, String content) {
        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            throw new IllegalArgumentException("公告标题和内容不能为空");
        }
        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setTitle(title);
        announcement.setContent(content);
        if (mapper.updateById(announcement) == 0) {
            throw new IllegalArgumentException("公告不存在");
        }
    }

    /** 修改公告是否发布。 */
    public void updateStatus(Long id, Integer status) {
        if (mapper.updateStatus(id, status) == 0) {
            throw new IllegalArgumentException("公告不存在");
        }
    }
}

