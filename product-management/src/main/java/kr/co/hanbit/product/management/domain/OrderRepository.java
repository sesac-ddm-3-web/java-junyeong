package kr.co.hanbit.product.management.domain;

import java.util.List;

public interface OrderRepository {
    void add(Order order);
    List<Order> findAll();
    List<kr.co.hanbit.product.management.presentation.OrderProductResponse> findAllWithProduct();
}
