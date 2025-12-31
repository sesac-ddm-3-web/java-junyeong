package kr.co.hanbit.product.management.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootTest(properties = "spring.datasource.url=jdbc:mysql://localhost:3307/product_management")
@ActiveProfiles("prod")
public class BulkInsertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void bulkInsert() {
        int totalCount = 740_000;
        int batchSize = 10_000;

        long startTime = System.currentTimeMillis();

        String sql = "INSERT INTO products (name, price, amount) VALUES (?, ?, ?)";

        for (int i = 0; i < totalCount; i += batchSize) {
            final int start = i;
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int j) throws SQLException {
                    ps.setString(1, "New_Product_" + (start + j));
                    ps.setInt(2, (int) (Math.random() * 100000));
                    ps.setInt(3, (int) (Math.random() * 1000));
                }

                @Override
                public int getBatchSize() {
                    return batchSize;
                }
            });
            System.out.println((i + batchSize) + "개 삽입 완료...");
        }

        long endTime = System.currentTimeMillis();
        System.out.println("------------------------------------");
        System.out.println("총 " + totalCount + "건 삽입 소요 시간: " + (endTime - startTime) / 1000.0 + "초");
        System.out.println("------------------------------------");
    }
}
