package kr.co.hanbit.product.management.infrastructure;

import java.util.List;
import java.util.Optional;
import kr.co.hanbit.product.management.domain.entity.User;
import kr.co.hanbit.product.management.domain.entity.UserRepository;
import kr.co.hanbit.product.management.domain.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Profile("prod")
public class JdbcUserRepository implements UserRepository {

  private final JdbcTemplate jdbcTemplate;

  private RowMapper<User> rowMapper = (rs, rowNum) -> {
    return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
        rs.getString("password"));
  };

  @Override
  public User add(User user) {
    String sql = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      java.sql.PreparedStatement ps = connection.prepareStatement(sql,
          java.sql.Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getPassword());
      return ps;
    }, keyHolder);

    Long userId = keyHolder.getKey().longValue();
    user.setId(userId);

    return user;
  }

  @Override
  public User update(User user) {
    String sql = "UPDATE user SET name = ?, email = ?, password = ? WHERE id = ?";

    int affectedRows = jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(),
        user.getId());

    if (affectedRows == 0) {
      throw new EntityNotFoundException("User를 찾지 못했습니다.");
    }
    return user;
  }

  @Override
  public void delete(Long userId) {
    String sql = "DELETE FROM user WHERE id = ?";

    jdbcTemplate.update(sql, userId);
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    String sql = "SELECT * FROM user WHERE email = ?";

    List<User> responses = jdbcTemplate.query(sql, rowMapper, email);

    if (responses.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(responses.get(0));
  }

  @Override
  public Optional<User> findUserById(Long userId) {
    String sql = "SELECT * FROM user WHERE id = ?";
    List<User> responses = jdbcTemplate.query(sql, rowMapper, userId);
    if (responses.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(responses.get(0));
  }
}
