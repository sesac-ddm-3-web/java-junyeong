package kr.co.ordermanagement.infrastructure;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import kr.co.ordermanagement.domain.exception.EntityNotFoundException;
import kr.co.ordermanagement.domain.order.Order;
import kr.co.ordermanagement.domain.order.OrderRepository;
import kr.co.ordermanagement.domain.order.OrderStatus;
import kr.co.ordermanagement.domain.order.OrderedProduct;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jdbc")
public class JdbcOrderRepository implements OrderRepository {

  private final JdbcTemplate jdbcTemplate;

  public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private final RowMapper<OrderedProduct> orderedProductRowMapper = (rs, rowNum) -> {
    return new OrderedProduct(
        rs.getLong("product_id"),
        rs.getString("name"),
        rs.getInt("price"),
        rs.getInt("amount")
    );
  };

  @Override
  // Repository의 add는 Order라는 domain model을 받아서 실제 DB에 저장하는 데이터 영속화를 담당한다. -> 쿼리를 작성하고, 실제 쿼리를 날리고 받은 데이터로 domain model에 id로 할당함(KeyHolder)
  public Order add(Order order) {
    String orderSql = "INSERT INTO orders (total_price, state) VALUES (?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder(); // DB가 AUTO_INCREMENT로 자동 생성한 primary_key id를 가져오기 위해 JDBC가 제공하는 인터페이스

    jdbcTemplate.update(connection -> {
      // PreparedStatement -> DB에 SQL문을 전달하고 실행하는 인터페이스 기존 Statement의 문제점 2가지를 개선한 인터페이스가 PreparedStatement
      // 이해하지 말고 받아들이자. SQL 던져주는 인터페이스구나! 두가지 문제점 보완했구나 1)똑같은 쿼리가 매번 재실행되는 문제 2)SQL 인젝션(?이 들어갈 자리에 DROP DATABASE가 들어가면?)
      PreparedStatement ps = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
      ps.setInt(1, order.getTotalPrice());
      ps.setString(2, order.getState().toString());
      return ps;
    }, keyHolder);

    Long orderId = keyHolder.getKey().longValue();
    order.setId(orderId); // DB가 만든 ID = KeyHolder를 서버의 데이터

    // order에 있는 ordered_products도 DB에 영속화 시켜줘야 함
    String orderedProductSql = "INSERT INTO ordered_products (order_id, product_id, name, price, amount) VALUES (?, ?, ?, ?, ?)";

    for (OrderedProduct orderedProduct : order.getOrderedProducts()) {
      jdbcTemplate.update(orderedProductSql, orderId, orderedProduct.getId(),
          orderedProduct.getName(), orderedProduct.getPrice(), orderedProduct.getAmount());
    }

    return order;
  }

  @Override
  public Optional<Order> findById(Long id) {
    String orderSql = "SELECT id, total_price, state FROM orders WHERE id = ?";

    List<Order> orders = jdbcTemplate.query(orderSql, (rs, rowNum) -> {
      // 사실 Long id와 같은 값이지만 서버가 클라이언트에게 받은 데이터가 실제 DB의 데이터와 동일하다고 볼 수 없기 때문에, 이러한 데이터 매핑을 해주는 책임이 Repo의 로직에 있어야 한다. (쿼리로 order를 받아와 그 안에 order_products를 가져오는 것) -> 일치 하지 않으면 옵셔널 던짐
      Long orderId = rs.getLong("id");
      OrderStatus state = OrderStatus.valueOf(rs.getString("state"));

      String orderedProductSql = "SELECT product_id, name, price, amount FROM ordered_products WHERE order_id = ?";
      List<OrderedProduct> orderedProducts = jdbcTemplate.query(orderedProductSql,
          orderedProductRowMapper, orderId);

      Order order = new Order(orderedProducts);
      order.setId(orderId);
      order.changeStateForce(state);

      return order;
    }, id);

    if (orders.isEmpty()) {
      throw new EntityNotFoundException("Order를 찾지 못했습니다.");
    }

    return Optional.of(orders.get(0));
  }

  @Override
  public List<Order> findByStatus(OrderStatus orderStatus) {
    String orderSql = "SELECT * FROM orders WHERE state = ?";

    return jdbcTemplate.query(orderSql, (rs, rowNum) -> {
      Long orderId = rs.getLong("order_id");
      OrderStatus state = OrderStatus.valueOf(rs.getString("state"));

      String orderedProductSql = "SELECT product_id, name, price, amount FROM ordered_products WHERE order_id = ?";
      List<OrderedProduct> orderedProducts = jdbcTemplate.query(orderedProductSql,
          orderedProductRowMapper, orderId);

      Order order = new Order(orderedProducts);
      order.setId(orderId);
      order.changeStateForce(state);

      return order;
    }, orderStatus.name());
  }
}
