package spring_practice.simple_board_service.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import spring_practice.simple_board_service.domain.User;
import spring_practice.simple_board_service.domain.UserRepository;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {

  private final JdbcTemplate jdbcTemplate;

  public final RowMapper<User> userRowMapper = ((rs, rowNum) -> {
    return new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"),
        rs.getString("password"));
  });


  @Override
  public User add(User user) {
    String sql = "INSERT INTO user (name, email, password) VALUES (?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
  public Optional<User> findByEmail(String email) {
    String sql = "SELECT * FROM user WHERE email = ?";

    try {
      // queryForObject: 결과가 반드시 1개여야 할 때 사용. 0개일 경우 EmptyResultDataAccessException 발생.
      User user = jdbcTemplate.queryForObject(sql, userRowMapper, email);
      return Optional.ofNullable(user);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> findByUserId(Long userId) {
    String sql = "SELECT * FROM user WHERE id = ?";

    try {
      // queryForObject: 결과가 반드시 1개여야 할 때 사용. 0개일 경우 EmptyResultDataAccessException 발생.
      User user = jdbcTemplate.queryForObject(sql, userRowMapper, userId);
      return Optional.ofNullable(user);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

}
