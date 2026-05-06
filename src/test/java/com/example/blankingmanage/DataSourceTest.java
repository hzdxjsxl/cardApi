package com.example.blankingmanage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class DataSourceTest {

    @Autowired(required = false) // 允许为空，防止直接报错
    private DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        if (dataSource == null) {
            System.err.println("🚩🚩🚩 严重警告：DataSource 是 null！Spring 根本没读取到数据库配置！请检查 application.yml 位置和缩进！");
        } else {
            System.out.println("✅✅✅ 数据库连接成功！类型：" + dataSource.getClass());
            System.out.println("✅✅✅ 连接测试：" + dataSource.getConnection());
        }
    }
}