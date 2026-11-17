/**
 * 文件说明：登录请求参数对象。
 * 作用：接收用户输入的账号和密码。
 */
package com.cs.cs_hotpot_ordering_system.dto;

public record LoginRequest(String username, String password) { }
