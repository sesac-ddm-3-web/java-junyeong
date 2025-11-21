package kr.co.ordermanagement.infrastructure;

import java.util.List;
import kr.co.ordermanagement.domain.exception.EntityNotFoundException;
import kr.co.ordermanagement.domain.product.Product;
import kr.co.ordermanagement.domain.product.ProductRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jdbc")
public class JdbcProductRepository implements ProductRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcProductRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<Product> productRowMapper = (rs, rowNum) -> {
    return new Product(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getInt("price"),
        rs.getInt("amount")
    );
  };

  @Override
  public Product findById(Long id) {
    String sql = "SELECT id, name, price, amount FROM products WHERE id = ?";
    List<Product> products = jdbcTemplate.query(sql, productRowMapper, id);

    if (products.isEmpty()) {
      throw new EntityNotFoundException("Product를 찾지 못했습니다.");
    }

    return products.get(0);
  }

  @Override
  public List<Product> findAll() {
    String sql = "SELECT id, name, price, amount FROM products";
    return jdbcTemplate.query(sql, productRowMapper);
  }

  @Override
  public void update(Product product) {
    String sql = "UPDATE products SET name = ?, price = ?, amount = ? WHERE id = ?";
    jdbcTemplate.update(sql,
        product.getName(),
        product.getPrice(),
        product.getAmount(),
        product.getId());
  }
}