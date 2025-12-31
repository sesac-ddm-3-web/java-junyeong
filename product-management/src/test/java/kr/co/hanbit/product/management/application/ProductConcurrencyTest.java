package kr.co.hanbit.product.management.application;

import kr.co.hanbit.product.management.domain.Product;
import kr.co.hanbit.product.management.domain.ProductRepository;
import kr.co.hanbit.product.management.presentation.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.datasource.url=jdbc:mysql://localhost:3307/product_management")
@ActiveProfiles("prod")
public class ProductConcurrencyTest {

    @Autowired
    private SimpleProductService simpleProductService;

    @Autowired
    private ProductRepository productRepository;

    private Long productId;

    @BeforeEach
    void setUp() {
        // 테스트 시작 전 기존 데이터 정리 (멱등성 보장)
        // 실제 운영 DB를 쓸 때는 조심해야 하지만, 여기선 테스트용 Docker DB라 가정
        try {
            // 간단히 이름으로 찾아서 지우거나, 매번 새로 생성
        } catch (Exception e) {}

        ProductDto productDto = new ProductDto("동시성 테스트 상품", 10000, 100);
        ProductDto savedProduct = simpleProductService.add(productDto);
        productId = savedProduct.getId();
    }

    @Test
    @DisplayName("1. [실패 케이스] 일반적인 조회/수정 로직은 동시성 문제가 발생하여 재고가 0이 되지 않는다.")
    void concurrencyFailTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    // 락 없이 호출
                    simpleProductService.decreaseAmount(productId, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();

        Product product = productRepository.findById(productId);
        System.out.println("[Fail Test] 최종 재고: " + product.getAmount());
        
        // 실패해야 정상 (재고가 남아있어야 함)
        assertThat(product.getAmount()).isGreaterThan(0);
    }

    @Test
    @DisplayName("2. [성공 케이스] 비관적 락(Pessimistic Lock)을 적용하면 동시성 문제가 해결되어 재고가 0이 된다.")
    void concurrencySuccessTest() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    // 락 적용 메서드 호출
                    simpleProductService.decreaseAmountWithLock(productId, 1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        doneLatch.await();

        Product product = productRepository.findById(productId);
        System.out.println("[Success Test] 최종 재고: " + product.getAmount());

        // 성공해야 함 (재고가 0이어야 함)
        assertThat(product.getAmount()).isEqualTo(0);
    }
}