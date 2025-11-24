package spring_practice.simple_board_service.domain;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {

  public User add(User user);

  public Optional<User> findByUserId(Long userId);

  public Optional<User> findByEmail(String email);
}
