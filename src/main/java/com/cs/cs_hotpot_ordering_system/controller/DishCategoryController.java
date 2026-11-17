/**
 * 文件说明：菜品分类控制器。
 * 作用：为管理员提供分类的查询、新增、修改和停用功能。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.entity.DishCategory;
import com.cs.cs_hotpot_ordering_system.service.DishCategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dishCategories")
public class DishCategoryController {
    private final DishCategoryService service;
    public DishCategoryController(DishCategoryService service) { this.service = service; }
    /** 查询所有启用的菜品分类。 */
    @GetMapping public ApiResponse<?> list() { return ApiResponse.success(service.list()); }
    /** 新增一个菜品分类。 */
    @PostMapping public ApiResponse<?> add(@RequestBody DishCategory category) { service.add(category); return ApiResponse.success(null); }
    /** 修改分类名称或排序号。 */
    @PutMapping("/{id}") public ApiResponse<?> update(@PathVariable("id") Long id,@RequestBody DishCategory category) { service.update(id,category); return ApiResponse.success(null); }
    /** 逻辑删除分类，不直接删除数据库记录。 */
    @DeleteMapping("/{id}") public ApiResponse<?> remove(@PathVariable("id") Long id) { service.remove(id); return ApiResponse.success(null); }
}
