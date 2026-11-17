/**
 * 文件说明：订单业务层。
 * 作用：完成订单查询、购物车结算、订单明细保存和订单状态流转。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.dto.OrderSubmitRequest;
import com.cs.cs_hotpot_ordering_system.entity.HotPotOrder;
import com.cs.cs_hotpot_ordering_system.entity.OrderItem;
import com.cs.cs_hotpot_ordering_system.mapper.CartItemMapper;
import com.cs.cs_hotpot_ordering_system.mapper.DishMapper;
import com.cs.cs_hotpot_ordering_system.mapper.OrderItemMapper;
import com.cs.cs_hotpot_ordering_system.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartItemMapper cartItemMapper;
    private final DishMapper dishMapper;

    public OrderService(OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper,
                        CartItemMapper cartItemMapper,
                        DishMapper dishMapper) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartItemMapper = cartItemMapper;
        this.dishMapper = dishMapper;
    }

    /** 查询当前用户的订单。 */
    public List<Map<String, Object>> mine(Long userId) {
        return orderMapper.findByUserId(userId);
    }

    /** 查询全部订单，供管理员使用。 */
    public List<Map<String, Object>> all() {
        return orderMapper.findAll();
    }

    /** 查询订单明细。 */
    public List<Map<String, Object>> items(Long orderId) {
        return orderMapper.findItems(orderId);
    }

    /** 提交订单：保存订单、订单明细，并清空购物车。 */
    @Transactional
    public Long submit(OrderSubmitRequest request) {
        if (request == null || request.userId() == null) {
            throw new IllegalArgumentException("用户信息不能为空");
        }

        List<Map<String, Object>> cartItems = orderMapper.findCartItems(request.userId());
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("购物车为空，无法提交订单");
        }

        BigDecimal total = cartItems.stream()
                .map(item -> decimalValue(item.get("subtotal")))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        HotPotOrder order = new HotPotOrder();
        order.setOrderNumber("HP" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        order.setUserId(request.userId());
        order.setTableId(request.tableId());
        order.setTotalAmount(total);
        order.setOrderStatus("COOKING");
        order.setRemark(request.remark());

        if (orderMapper.insert(order) != 1 || order.getId() == null) {
            throw new IllegalStateException("订单保存失败");
        }

        for (Map<String, Object> cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setDishId(longValue(cartItem.get("dishId")));
            orderItem.setDishName(String.valueOf(cartItem.get("dishName")));
            orderItem.setUnitPrice(decimalValue(cartItem.get("price")));
            orderItem.setQuantity(integerValue(cartItem.get("quantity")));
            orderItem.setSubtotal(decimalValue(cartItem.get("subtotal")));

            if (orderItemMapper.insert(orderItem) != 1) {
                throw new IllegalStateException("订单明细保存失败");
            }

            // 用户提交订单后立即完成销量统计，不再等待管理员确认。
            if (dishMapper.increaseSaleNumber(orderItem.getDishId(), orderItem.getQuantity()) == 0) {
                throw new IllegalStateException("菜品销量更新失败");
            }
        }

        cartItemMapper.clearByUser(request.userId());
        return order.getId();
    }

    /**
     * 校验并修改订单状态。
     * 订单第一次从待确认变为确认、制作中或已完成时，才累加一次菜品销量。
     * 这样管理员重复提交状态或继续流转状态时不会重复统计销量。
     */
    @Transactional
    public void updateStatus(Long id, String orderStatus) {
        if (!List.of("CONFIRMED", "COOKING", "COMPLETED", "CANCELLED")
                .contains(orderStatus)) {
            throw new IllegalArgumentException("订单状态不正确");
        }

        HotPotOrder order = orderMapper.selectById(id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在");
        }

        String oldStatus = order.getOrderStatus();
        boolean shouldIncreaseSales = "PENDING".equalsIgnoreCase(oldStatus)
                && !"CANCELLED".equalsIgnoreCase(orderStatus);

        if (orderMapper.updateStatus(id, orderStatus) == 0) {
            throw new IllegalArgumentException("订单不存在");
        }

        if (shouldIncreaseSales) {
            List<Map<String, Object>> items = orderMapper.findItems(id);
            for (Map<String, Object> item : items) {
                Long dishId = longValue(item.get("dishId"));
                Integer quantity = integerValue(item.get("quantity"));
                if (dishMapper.increaseSaleNumber(dishId, quantity) == 0) {
                    throw new IllegalStateException("菜品销量更新失败");
                }
            }
        }
    }
    /** 将数据库查询值转换为金额。 */
    private static BigDecimal decimalValue(Object value) {
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        return new BigDecimal(String.valueOf(value));
    }

    /** 将数据库查询值转换为整数。 */
    private static Integer integerValue(Object value) {
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(String.valueOf(value));
    }

    /** 将数据库查询值转换为长整数。 */
    private static Long longValue(Object value) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        return Long.valueOf(String.valueOf(value));
    }
}
