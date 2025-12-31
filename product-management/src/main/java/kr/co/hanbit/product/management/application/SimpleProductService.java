package kr.co.hanbit.product.management.application;

import kr.co.hanbit.product.management.domain.OrderRepository;
import kr.co.hanbit.product.management.domain.Product;
import kr.co.hanbit.product.management.domain.ProductRepository;
import kr.co.hanbit.product.management.presentation.ProductDto;
import kr.co.hanbit.product.management.presentation.OrderProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SimpleProductService {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private ModelMapper modelMapper;
    private ValidationService validationService;

    @Autowired
    SimpleProductService(ProductRepository productRepository, 
                         OrderRepository orderRepository,
                         ModelMapper modelMapper, 
                         ValidationService validationService
    ) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
    }

    public List<kr.co.hanbit.product.management.presentation.OrderProductResponse> findAllOrderResponses() {
        List<kr.co.hanbit.product.management.domain.Order> orders = orderRepository.findAll(); // 1번: SELECT * FROM orders

        return orders.stream()
                .map(order -> {
                    // N번: 각 주문마다 상품 정보를 개별 조회 (N+1 문제 발생)
                    Product product = productRepository.findById(order.getProductId());
                    return new kr.co.hanbit.product.management.presentation.OrderProductResponse(
                            order.getId(),
                            product.getName(),
                            order.getQuantity()
                    );
                }).toList();
    }

    public List<kr.co.hanbit.product.management.presentation.OrderProductResponse> findAllOrderResponsesOptimized() {
        // 단 1번의 JOIN 쿼리로 모든 정보를 가져옴
        return orderRepository.findAllWithProduct();
    }

    public ProductDto add(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        validationService.checkValid(product);

        Product savedProduct = productRepository.add(product);
        ProductDto savedProductDto = modelMapper.map(savedProduct, ProductDto.class);
        return savedProductDto;
    }

    public ProductDto findById(Long id) {
        Product product = productRepository.findById(id);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return productDtos;
    }

    public List<ProductDto> findByNameContaining(String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return productDtos;
    }

    public ProductDto update(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        Product updatedProduct = productRepository.update(product);
        ProductDto updatedProductDto = modelMapper.map(updatedProduct, ProductDto.class);
        return updatedProductDto;
    }



    public void delete(Long id) {
        productRepository.delete(id);
    }

    public void decreaseAmount(Long id, Integer quantity) {
        Product product = productRepository.findById(id);
        product.decreaseAmount(quantity);
        productRepository.update(product);
    }

    @Transactional
    public void decreaseAmountWithLock(Long id, Integer quantity) {
        Product product = productRepository.findByIdWithLock(id);
        product.decreaseAmount(quantity);
        productRepository.update(product);
    }
}
