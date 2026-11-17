/**
 * 文件说明：用户管理业务层。
 * 作用：查询用户、修改用户状态和更新个人资料，同时对密码进行脱敏。
 */
package com.cs.cs_hotpot_ordering_system.service;

import com.cs.cs_hotpot_ordering_system.entity.User;
import com.cs.cs_hotpot_ordering_system.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserMapper mapper;
    public UserService(UserMapper mapper) { this.mapper = mapper; }
    /** 查询用户列表，并清除密码字段，防止密码返回到前端。 */
    public List<User> list() { List<User> users = mapper.findAll(); users.forEach(user -> user.setPassword(null)); return users; }
    /** 启用或禁用用户。 */
    public void updateStatus(Long id, Integer status) { if (status == null || (status != 0 && status != 1)) throw new IllegalArgumentException("用户状态不正确"); mapper.updateStatus(id,status); }
    /** 查询个人资料，并清除密码字段后返回前端。 */
    public User profile(Long id) {
        User user = mapper.findById(id);
        if (user == null) throw new IllegalArgumentException("用户不存在");
        user.setPassword(null);
        return user;
    }

    /** 修改用户昵称、电话和头像。 */
    public void updateProfile(Long id, String nickname, String phone, String avatar) {
        if (nickname == null || nickname.isBlank()) throw new IllegalArgumentException("昵称不能为空");
        if (mapper.updateProfile(id, nickname.trim(), phone, avatar) == 0) {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    /** 修改密码前校验旧密码，避免误改其他用户的密码。 */
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        if (oldPassword == null || oldPassword.isBlank()) {
            throw new IllegalArgumentException("请输入当前密码");
        }
        if (newPassword == null || newPassword.length() < 4) {
            throw new IllegalArgumentException("新密码至少4位");
        }
        User user = mapper.findById(id);
        if (user == null) throw new IllegalArgumentException("用户不存在");
        if (!oldPassword.equals(user.getPassword())) {
            throw new IllegalArgumentException("当前密码不正确");
        }
        if (mapper.updatePassword(id, newPassword) == 0) {
            throw new IllegalArgumentException("密码修改失败");
        }
    }
}
