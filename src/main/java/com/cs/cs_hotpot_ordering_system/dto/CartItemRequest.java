/**
 * 文件说明：购物车请求参数对象。
 * 作用：接收用户编号、菜品编号和购买数量。
 */
package com.cs.cs_hotpot_ordering_system.dto;

public record CartItemRequest(Long userId, Long dishId, Integer quantity) { }
