package com.cs.cs_hotpot_ordering_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 文件说明：项目启动测试类。
 * 作用：验证 Spring 容器能否正常启动（数据库连不上、Bean 配置错误等都会在这里暴露）。
 * 注意：运行该测试需要本地 MySQL 已启动并建好 cs_hotpot 数据库。
 */
@SpringBootTest
class CsHotPotOrderingSystemApplicationTests {

    /** 空测试方法：只要容器成功启动即算通过。 */
    @Test
    void contextLoads() {
    }

}
