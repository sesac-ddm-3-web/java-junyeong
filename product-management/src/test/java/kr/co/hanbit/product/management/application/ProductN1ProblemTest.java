package kr.co.hanbit.product.management.application;

import kr.co.hanbit.product.management.domain.Order;
import kr.co.hanbit.product.management.domain.OrderRepository;
import kr.co.hanbit.product.management.domain.Product;
import kr.co.hanbit.product.management.domain.ProductRepository;
import kr.co.hanbit.product.management.presentation.OrderProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("prod")
public class ProductN1ProblemTest {

    @Autowired
    private SimpleProductService simpleProductService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("N+1 문제 발생 확인 테스트 (데이터 1000개)")
    void testNPlusOneProblem() {
        // 1. 데이터 준비 (상품 1000개, 주문 1000개)
        int dataCount = 1000;
        for (int i = 1; i <= dataCount; i++) {
            Product product = new Product();
            product.setName("Product_" + i);
            product.setPrice(1000);
            product.setAmount(100);
            Product savedProduct = productRepository.add(product);

            Order order = new Order();
            order.setProductId(savedProduct.getId());
            order.setQuantity(i);
            orderRepository.add(order);
        }

        System.out.println("=========================================");
        System.out.println(">>> [N+1 문제 발생 구간 시작]");
        
        long startTime = System.currentTimeMillis();
        
        // 2. 비즈니스 로직 실행 (여기서 쿼리가 1 + 1000 = 1001번 발생)
        List<OrderProductResponse> responses = simpleProductService.findAllOrderResponses();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println(">>> [N+1 문제 발생 구간 종료]");
        System.out.println("=========================================");
        System.out.println("조회된 주문 개수: " + responses.size());
        System.out.println("소요 시간: " + (endTime - startTime) + "ms");
    }

    @Test
    @DisplayName("N+1 문제 최적화 확인 테스트 (JOIN 사용)")
    void testNPlusOneProblemOptimized() {
        // 데이터는 이미 위 테스트나 DB에 존재한다고 가정 (혹은 필요시 추가)
        
        System.out.println("=========================================");
        System.out.println(">>> [JOIN 최적화 구간 시작]");
        
        long startTime = System.currentTimeMillis();
        
        // 2. 최적화된 비즈니스 로직 실행 (쿼리 1번 발생)
        List<OrderProductResponse> responses = simpleProductService.findAllOrderResponsesOptimized();
        
        long endTime = System.currentTimeMillis();
        
        System.out.println(">>> [JOIN 최적화 구간 종료]");
        System.out.println("=========================================");
        System.out.println("조회된 주문 개수: " + responses.size());
        System.out.println("소요 시간: " + (endTime - startTime) + "ms");
    }
}
