package kr.co.hanbit.product.management.infrastructure;

import kr.co.hanbit.product.management.domain.Order;
import kr.co.hanbit.product.management.domain.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("prod")
public class DatabaseOrderRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public DatabaseOrderRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public void add(Order order) {
        SqlParameterSource namedParameter = new BeanPropertySqlParameterSource(order);
        namedParameterJdbcTemplate.update(
                "INSERT INTO orders (product_id, quantity) VALUES (:productId, :quantity)", 
                namedParameter
        );
    }

    @Override
    public List<Order> findAll() {
        return namedParameterJdbcTemplate.query(
                "SELECT id, product_id as productId, quantity FROM orders",
                new BeanPropertyRowMapper<>(Order.class)
        );
    }

    @Override
    public List<kr.co.hanbit.product.management.presentation.OrderProductResponse> findAllWithProduct() {
        return namedParameterJdbcTemplate.query(
                "SELECT o.id as orderId, p.name as productName, o.quantity as quantity " +
                        "FROM orders o " +
                        "JOIN products p ON o.product_id = p.id",
                (rs, rowNum) -> new kr.co.hanbit.product.management.presentation.OrderProductResponse(
                        rs.getLong("orderId"),
                        rs.getString("productName"),
                        rs.getInt("quantity")
                )
        );
    }
}
