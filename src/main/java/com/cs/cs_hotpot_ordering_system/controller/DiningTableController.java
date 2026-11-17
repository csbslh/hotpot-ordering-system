/**
 * 文件说明：桌台管理控制器。
 * 作用：查询桌台、添加桌台和修改桌台使用状态。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.entity.DiningTable;
import com.cs.cs_hotpot_ordering_system.service.DiningTableService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/diningTables")
public class DiningTableController {
    private final DiningTableService service;
    public DiningTableController(DiningTableService service) { this.service = service; }
    /** 查询所有桌台及其当前状态。 */
    @GetMapping public ApiResponse<?> list() { return ApiResponse.success(service.list()); }
    /** 新增一个桌台。 */
    @PostMapping public ApiResponse<?> add(@RequestBody DiningTable table) { service.add(table); return ApiResponse.success(null); }
    /** 修改桌台状态，例如空闲、用餐中或已预订。 */
    @PutMapping("/{id}/status") public ApiResponse<?> status(@PathVariable("id") Long id,@RequestParam("tableStatus") String tableStatus) { service.updateStatus(id,tableStatus); return ApiResponse.success(null); }
}
