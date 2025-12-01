package spring_practice.simple_board_service.infrastructure;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import spring_practice.simple_board_service.domain.comment.Comment;
import spring_practice.simple_board_service.domain.comment.CommentRepository;

@Repository
@Profile("jdbc")
@RequiredArgsConstructor
public class JdbcCommentRepository implements CommentRepository {

  private final JdbcTemplate jdbcTemplate;

  public RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
    return new Comment(rs.getLong("id"), rs.getLong("article_id"), rs.getLong("author_id"),
        rs.getString("text"), rs.getTimestamp("created_at").toLocalDateTime());
  };

  @Override
  public List<Comment> findAllByArticleId(Long articleId) {
    String sql = "SELECT * FROM comment WHERE article_id = ?";
    return jdbcTemplate.query(sql, commentRowMapper, articleId);
  }

  @Override
  public Optional<Comment> findById(Long commentId) {
    String sql = "SELECT * FROM comment WHERE id = ?";

    List<Comment> response = jdbcTemplate.query(sql, commentRowMapper, commentId);

    if (response.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.of(response.get(0));
    }
  }

  @Override
  public Comment add(Comment comment) {
    String sql = "INSERT INTO comment (article_id, author_id, text, created_at) VALUES (?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setLong(1, comment.getArticleId());
      ps.setLong(2, comment.getAuthorId());
      ps.setString(3, comment.getText());
      ps.setTimestamp(4, java.sql.Timestamp.valueOf(comment.getCreatedAt()));
      return ps;
    }, keyHolder);

    Long commentId = keyHolder.getKey().longValue();
    comment.setId(commentId);

    return comment;
  }

  @Override
  public Comment update(Comment updateComment) {
    String sql = "UPDATE comment SET text = ? WHERE id = ?";

    jdbcTemplate.update(sql, updateComment.getText(), updateComment.getId());

    String newCommentSql = "SELECT * FROM comment WHERE id = ?";

    List<Comment> response = jdbcTemplate.query(newCommentSql, commentRowMapper,
        updateComment.getId());

    return response.get(0);
  }

  @Override
  public void delete(Long CommentId) {
    String sql = "DELETE FROM comment WHERE id = ?";

    jdbcTemplate.update(sql, CommentId);

    return;
  }
}
