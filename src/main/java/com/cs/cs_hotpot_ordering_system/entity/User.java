/**
 * 文件说明：用户实体类，对应数据库中的 user 表。
 * 作用：保存普通用户和管理员的账号、个人资料及状态信息。
 */
package com.cs.cs_hotpot_ordering_system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("`user`")
public class User {
    /** 用户主键。 */
    @TableId(type = IdType.AUTO)
    private Long id;
    /** 登录账号。 */
    private String username;
    /** 登录密码，接口返回时会清空该字段。 */
    private String password;
    /** 页面显示的昵称。 */
    private String nickname;
    /** 联系手机号。 */
    private String phone;
    /** 用户头像地址。 */
    private String avatar;
    /** 用户角色：USER 表示普通用户，ADMIN 表示管理员。 */
    private String role;
    /** 用户状态：1 启用，0 禁用。 */
    private Integer status;
    /** 创建时间。 */
    private LocalDateTime createdAt;
    /** 最后更新时间。 */
    private LocalDateTime updatedAt;
}
