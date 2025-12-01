package spring_practice.simple_board_service.domain.comment;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository {

  public List<Comment> findAllByArticleId(Long articleId);

  public Optional<Comment> findById(Long commentId);

  public Comment add(Comment comment);

  public Comment update(Comment comment);

  public void delete(Long CommentId);

}
