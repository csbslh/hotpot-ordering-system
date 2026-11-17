/**
 * 文件说明：订单控制器。
 * 作用：提供用户订单查询、订单提交、订单明细查询和管理员状态处理接口。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.dto.OrderSubmitRequest;
import com.cs.cs_hotpot_ordering_system.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;
    public OrderController(OrderService service) { this.service = service; }
    /** userId 为空时用于管理员查询全部订单，否则查询指定用户的订单。 */
    @GetMapping public ApiResponse<?> all(@RequestParam(value="userId", required=false) Long userId) { return ApiResponse.success(userId == null ? service.all() : service.mine(userId)); }
    /** 查询一个订单中的菜品明细。 */
    @GetMapping("/{id}/items") public ApiResponse<?> items(@PathVariable("id") Long id) { return ApiResponse.success(service.items(id)); }
    /** 根据购物车内容提交订单。 */
    @PostMapping public ApiResponse<?> submit(@RequestBody OrderSubmitRequest request) { return ApiResponse.success("订单提交成功",service.submit(request)); }
    /** 管理员修改订单处理状态。 */
    @PutMapping("/{id}/status") public ApiResponse<?> status(@PathVariable("id") Long id,@RequestParam("orderStatus") String orderStatus) { service.updateStatus(id,orderStatus); return ApiResponse.success(null); }
}
