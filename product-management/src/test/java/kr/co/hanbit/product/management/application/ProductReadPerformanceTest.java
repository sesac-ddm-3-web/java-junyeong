package kr.co.hanbit.product.management.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StopWatch;

@SpringBootTest(properties = "spring.datasource.url=jdbc:mysql://localhost:3307/product_management")
@ActiveProfiles("prod")
public class ProductReadPerformanceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("인덱스가 있는 컬럼(ID)과 없는 컬럼(Name)의 조회 성능 비교")
    void compareSelectPerformance() {
        StopWatch stopWatch = new StopWatch("조회 성능 비교");

        // 데이터가 26만건 정도 있다고 가정하고, 중간쯤에 있는 데이터를 타겟으로 잡습니다.
        // (Bulk Insert 시 '상품_번호' 패턴으로 넣었다고 가정)
        long targetId = 150000L; 
        String targetName = "상품_" + targetId;

        // 1. 인덱스가 있는 ID로 조회 (Primary Key) -> 순식간에 끝남
        stopWatch.start("1. PK(ID)로 조회 (인덱스 O)");
        jdbcTemplate.queryForList("SELECT * FROM products WHERE id = ?", targetId);
        stopWatch.stop();

        // 2. 인덱스가 없는 Name으로 조회 -> 테이블 전체를 뒤져야 함 (Full Table Scan)
        stopWatch.start("2. Name으로 조회 (인덱스 X)");
        jdbcTemplate.queryForList("SELECT * FROM products WHERE name = ?", targetName);
        stopWatch.stop();

        // 3. Like 검색 (더 느림)
        stopWatch.start("3. Name Like 검색 (Full Scan)");
        jdbcTemplate.queryForList("SELECT * FROM products WHERE name LIKE ?", targetName + "%");
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }
}
