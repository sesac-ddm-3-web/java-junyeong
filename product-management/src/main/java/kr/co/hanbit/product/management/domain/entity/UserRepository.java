package kr.co.hanbit.product.management.domain.entity;

import java.util.Optional;

public interface UserRepository {

  public User add(User user);

  public User update(User user);

  public void delete(Long userId);

  public Optional<User> findUserByEmail(String email);

  public Optional<User> findUserById(Long userId);


}
