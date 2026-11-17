/**
 * 文件说明：用户认证控制器。
 * 作用：接收登录和注册请求，并把处理结果返回给前端。
 */
package com.cs.cs_hotpot_ordering_system.controller;

import com.cs.cs_hotpot_ordering_system.common.ApiResponse;
import com.cs.cs_hotpot_ordering_system.dto.LoginRequest;
import com.cs.cs_hotpot_ordering_system.dto.RegisterRequest;
import com.cs.cs_hotpot_ordering_system.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }
    /** 用户登录接口。 */
    @PostMapping("/login") public ApiResponse<?> login(@RequestBody LoginRequest request) { return ApiResponse.success(authService.login(request.username(),request.password())); }
    /** 普通用户注册接口。 */
    @PostMapping("/register") public ApiResponse<?> register(@RequestBody RegisterRequest request) { return ApiResponse.success("注册成功", authService.register(request.username(),request.password(),request.nickname(),request.phone())); }
}
