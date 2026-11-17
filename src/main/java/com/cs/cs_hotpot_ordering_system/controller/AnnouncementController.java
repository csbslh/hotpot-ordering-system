/**
 * 文件说明：公告管理控制器。
 * 作用：提供公告查询、发布和上下架接口，供普通用户和管理员使用。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    private final AnnouncementService service;
    public AnnouncementController(AnnouncementService service) { this.service = service; }
    /** 查询公告；管理员可以查看全部公告，普通用户只查看已发布公告。 */
    @GetMapping public ApiResponse<?> list(@RequestParam(value="admin", defaultValue="false") boolean admin) { return ApiResponse.success(admin ? service.all() : service.published()); }
    /** 新增并立即发布一条公告。 */
    @PostMapping public ApiResponse<?> add(@RequestBody Map<String,Object> body) { service.add(String.valueOf(body.get("title")),String.valueOf(body.get("content")),longValue(body.get("publisherId"))); return ApiResponse.success(null); }
    /** 修改公告标题和正文。 */
    @PutMapping("/{id}")
    public ApiResponse<?> update(@PathVariable("id") Long id, @RequestBody Map<String,Object> body) {
        service.update(id, String.valueOf(body.get("title")), String.valueOf(body.get("content")));
        return ApiResponse.success(null);
    }

    /** 修改公告发布状态。 */
    @PutMapping("/{id}/status") public ApiResponse<?> status(@PathVariable("id") Long id,@RequestParam("publishStatus") Integer publishStatus) { service.updateStatus(id,publishStatus); return ApiResponse.success(null); }
    private static Long longValue(Object value) { return value == null ? null : Long.valueOf(value.toString()); }
}

