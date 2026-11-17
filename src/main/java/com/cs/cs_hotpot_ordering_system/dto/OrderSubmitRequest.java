/**
 * 文件说明：提交订单请求参数对象。
 * 作用：接收下单用户、桌台和订单备注。
 */
package com.cs.cs_hotpot_ordering_system.dto;

public record OrderSubmitRequest(Long userId, Long tableId, String remark) { }
