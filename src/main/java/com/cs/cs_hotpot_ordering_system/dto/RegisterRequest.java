/**
 * 文件说明：注册请求参数对象。
 * 作用：接收新用户注册所需的基本信息。
 */
package com.cs.cs_hotpot_ordering_system.dto;

public record RegisterRequest(String username, String password, String nickname, String phone) { }
