package kr.co.hanbit.product.management.application;

import kr.co.hanbit.product.management.domain.Product;
import kr.co.hanbit.product.management.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("prod")
public class ProductSearchPerformanceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("인덱스 적용 후 이름 검색 성능 측정 및 실행 계획 분석")
    void searchPerformanceTest() {
        String searchName = "New_Product_739999";

        // 0. 인덱스 생성
        try {
            jdbcTemplate.execute("CREATE INDEX idx_products_name ON products (name)");
            System.out.println(">>> 인덱스 생성 완료: idx_products_name");
        } catch (Exception e) {
            System.out.println(">>> 인덱스 이미 존재함");
        }

        // 1. 실행 계획(EXPLAIN) 확인 (완전 일치 = 사용)
        System.out.println("========== [EXPLAIN 분석] ==========");
        try {
            String sql = "SELECT * FROM products WHERE name = '" + searchName + "'";
            String explainSql = "EXPLAIN " + sql;
            
            jdbcTemplate.query(explainSql, (rs) -> {
                int colCount = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= colCount; i++) {
                        System.out.print(rs.getMetaData().getColumnName(i) + ": " + rs.getString(i) + " | ");
                    }
                    System.out.println();
                }
                return null; 
            });
        } catch (Exception e) {
            System.out.println("EXPLAIN 실행 중 오류 발생: " + e.getMessage());
        }
        System.out.println("===================================");

        
        // 2. 성능 측정 시작
        long startTime = System.currentTimeMillis();
        
        // 완전 일치 검색으로 변경
        List<Product> products = jdbcTemplate.query(
            "SELECT * FROM products WHERE name = ?",
            new org.springframework.jdbc.core.BeanPropertyRowMapper<>(Product.class),
            searchName
        );
        
        long endTime = System.currentTimeMillis();
        // 3. 결과 출력
        System.out.println("------------------------------------");
        System.out.println("검색어: " + searchName);
        System.out.println("검색 결과 수: " + products.size());
        System.out.println("소요 시간: " + (endTime - startTime) + "ms");
        System.out.println("------------------------------------");
    }
}