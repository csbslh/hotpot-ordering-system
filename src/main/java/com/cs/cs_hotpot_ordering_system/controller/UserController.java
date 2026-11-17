/**
 * 文件说明：用户管理控制器。
 * 作用：管理员查询用户、启用或禁用用户；普通用户可更新个人资料。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    /** 查询用户列表，Service 层会清除密码字段。 */
    @GetMapping public ApiResponse<?> list() { return ApiResponse.success(service.list()); }

    /** 查询当前登录用户的个人资料，密码不会返回前端。 */
    @GetMapping("/{id}/profile")
    public ApiResponse<?> profile(@PathVariable("id") Long id) {
        return ApiResponse.success(service.profile(id));
    }

    /** 修改用户启用状态。 */
    @PutMapping("/{id}/status") public ApiResponse<?> status(@PathVariable("id") Long id,@RequestParam("status") Integer status) { service.updateStatus(id,status); return ApiResponse.success(null); }
    /** 修改用户昵称、电话和头像。 */
    @PutMapping("/{id}/profile")
    public ApiResponse<?> profile(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        service.updateProfile(id, text(body, "nickname"), text(body, "phone"), text(body, "avatar"));
        return ApiResponse.success(null);
    }

    /** 修改用户密码。 */
    @PutMapping("/{id}/password")
    public ApiResponse<?> password(@PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        service.updatePassword(id, text(body, "oldPassword"), text(body, "newPassword"));
        return ApiResponse.success(null);
    }

    /** 安全读取 JSON 字符串，避免 null 被转换成字符串“null”。 */
    private String text(Map<String, Object> body, String key) {
        Object value = body == null ? null : body.get(key);
        return value == null ? null : String.valueOf(value);
    }
}
