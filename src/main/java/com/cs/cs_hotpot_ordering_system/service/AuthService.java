/**
 * 文件说明：用户认证业务层。
 * 作用：完成登录校验、注册校验和用户信息脱敏。
 * 注意：示例项目暂时使用明文密码，正式系统应使用 BCrypt 等方式加密密码。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.User;
import com.cs.cs_hotpot_ordering_system.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserMapper userMapper;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /** 校验账号密码，成功后清空返回对象中的密码。 */
    public User login(String username, String password) {
        if (username == null || username.isBlank() || password == null) {
            throw new IllegalArgumentException("账号或密码不能为空");
        }
        User user = userMapper.findByUsername(username);
        if (user == null || !password.equals(user.getPassword()) || user.getStatus() == 0) {
            throw new IllegalArgumentException("账号或密码错误");
        }
        user.setPassword(null);
        return user;
    }

    /** 校验注册信息并创建普通用户。 */
    public User register(String username, String password, String nickname, String phone) {
        if (username == null || username.isBlank()
                || password == null || password.length() < 6) {
            throw new IllegalArgumentException("账号不能为空，密码至少6位");
        }
        if (userMapper.findByUsername(username) != null) {
            throw new IllegalArgumentException("账号已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname == null || nickname.isBlank() ? username : nickname);
        user.setPhone(phone);
        user.setRole("USER");

        if (userMapper.insert(user) != 1 || user.getId() == null) {
            throw new IllegalStateException("用户注册失败");
        }
        user.setPassword(null);
        return user;
    }
}
