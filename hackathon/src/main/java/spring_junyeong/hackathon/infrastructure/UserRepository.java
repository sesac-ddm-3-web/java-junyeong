package spring_junyeong.hackathon.infrastructure;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import spring_junyeong.hackathon.domain.User;

@Repository
public class UserRepository {

  private HashMap<Long, User> DB = new HashMap<>();
  private AtomicLong sequence = new AtomicLong(1L);

  public User add(User user) {
    user.setId(sequence.getAndIncrement());
    DB.put(user.getId(), user);
    return user;
  }

  public User findById(long id) {
    return DB.get(id);
  }

  public Optional<User> findByEmail(String email) {
    return DB.values().stream().filter(user -> user.checkEmail(email)).findFirst();
  }

  public List<User> findAll() {
    return DB.values().stream().toList();
  }


}
