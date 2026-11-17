/**
 * 文件说明：用户数据访问接口。
 * 作用：使用 MyBatis-Plus 完成账号查询、用户列表查询和资料修改。
 */
package com.cs.cs_hotpot_ordering_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cs.cs_hotpot_ordering_system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/** 用户表 Mapper。 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /** 根据登录账号查询用户。 */
    User findByUsername(@Param("username") String username);

    /** 根据用户主键查询用户。 */
    User findById(@Param("id") Long id);

    /** 查询用户列表。 */
    List<User> findAll();

    /** 启用或禁用用户。 */
    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status);

    /** 修改用户个人资料。 */
    int updateProfile(@Param("id") Long id,
                      @Param("nickname") String nickname,
                      @Param("phone") String phone,
                      @Param("avatar") String avatar);

    /** 修改用户登录密码。 */
    int updatePassword(@Param("id") Long id,
                       @Param("password") String password);
}
