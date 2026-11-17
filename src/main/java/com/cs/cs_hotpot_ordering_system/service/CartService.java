/**
 * 文件说明：购物车业务层。
 * 作用：校验购物车操作参数，并调用数据访问层完成购物车业务。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.mapper.CartItemMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CartService {
    private final CartItemMapper cartItemMapper;

    public CartService(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    /** 查询用户购物车。 */
    public List<Map<String, Object>> list(Long userId) {
        return cartItemMapper.findByUserId(userId);
    }

    /** 加入购物车，数量必须为正数。 */
    public void add(Long userId, Long dishId, Integer quantity) {
        if (userId == null || dishId == null || quantity == null || quantity < 1) {
            throw new IllegalArgumentException("购物车参数不正确");
        }
        cartItemMapper.add(userId, dishId, quantity);
    }

    /** 修改购物车数量。 */
    public void update(Long id, Long userId, Integer quantity) {
        if (quantity == null || quantity < 1) {
            throw new IllegalArgumentException("数量必须大于0");
        }
        if (cartItemMapper.updateQuantity(id, userId, quantity) == 0) {
            throw new IllegalArgumentException("购物车记录不存在");
        }
    }

    /** 删除购物车记录。 */
    public void remove(Long id, Long userId) {
        if (cartItemMapper.deleteItem(id, userId) == 0) {
            throw new IllegalArgumentException("购物车记录不存在");
        }
    }
}
