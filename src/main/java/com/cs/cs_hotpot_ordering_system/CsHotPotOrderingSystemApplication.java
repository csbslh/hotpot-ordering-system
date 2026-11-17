/**
 * 文件说明：Spring Boot 项目的启动类。
 * 作用：启动内置 Web 服务器，并自动扫描当前包下的 Controller、Service 和 Mapper 组件。
 */
package com.cs.cs_hotpot_ordering_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.cs.cs_hotpot_ordering_system.mapper")
public class CsHotPotOrderingSystemApplication {

    /**
     * 程序入口方法。
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(CsHotPotOrderingSystemApplication.class, args);
    }

}
