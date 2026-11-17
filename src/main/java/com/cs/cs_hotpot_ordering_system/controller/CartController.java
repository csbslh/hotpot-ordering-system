/**
 * 文件说明：购物车控制器。
 * 作用：处理菜品加入购物车、数量修改、查询和删除请求。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.dto.CartItemRequest;
import com.cs.cs_hotpot_ordering_system.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService service;
    public CartController(CartService service) { this.service = service; }
    /** 查询指定用户的购物车。 */
    @GetMapping public ApiResponse<?> list(@RequestParam("userId") Long userId) { return ApiResponse.success(service.list(userId)); }
    /** 将指定菜品加入购物车。 */
    @PostMapping public ApiResponse<?> add(@RequestBody CartItemRequest request) { service.add(request.userId(),request.dishId(),request.quantity()); return ApiResponse.success(null); }
    /** 修改购物车中某条记录的数量。 */
    @PutMapping("/{id}") public ApiResponse<?> update(@PathVariable("id") Long id,@RequestParam("userId") Long userId,@RequestParam("quantity") Integer quantity) { service.update(id,userId,quantity); return ApiResponse.success(null); }
    /** 删除购物车中的一条记录。 */
    @DeleteMapping("/{id}") public ApiResponse<?> remove(@PathVariable("id") Long id,@RequestParam("userId") Long userId) { service.remove(id,userId); return ApiResponse.success(null); }
}
