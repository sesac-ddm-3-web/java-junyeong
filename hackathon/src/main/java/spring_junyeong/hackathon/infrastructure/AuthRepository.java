package spring_junyeong.hackathon.infrastructure;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import spring_junyeong.hackathon.domain.AuthStatus;
import spring_junyeong.hackathon.domain.User;

@Repository
public class AuthRepository {

  private HashMap<Long, AuthStatus> DB = new HashMap<>();

  public String add(AuthStatus authStatus) {
    DB.put(authStatus.getUserId(), authStatus);
    return authStatus.getToken();
  }

  public AuthStatus renew(User user) {
    AuthStatus authStatus = DB.get(user.getId());
    authStatus.renewAccess();
    return authStatus;
  }

  public AuthStatus revoke(User user) {
    AuthStatus authStatus = DB.get(user.getId());
    authStatus.revokeAccess();
    return authStatus;
  }

  public List<AuthStatus> findAll() {
    return DB.values().stream().toList();
  }

  public AuthStatus getStatus(User user) {
    return DB.get(user.getId());
  }

  public Boolean accessIsValid(User user) {
    AuthStatus status = DB.get(user.getId());
    return status.getIsValid();
  }

  public LocalDateTime getExpiredAt(User user) {
    return DB.get(user.getId()).getExpiredAt();
  }

}
