package spring_practice.simple_board_service.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import spring_practice.simple_board_service.common.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.domain.article.Article;
import spring_practice.simple_board_service.domain.article.ArticleRepository;

@Repository
@RequiredArgsConstructor
@Profile("jdbc")
public class JdbcArticleRepository implements ArticleRepository {

  private final JdbcTemplate jdbcTemplate;

  public RowMapper<Article> articleRowMapper = (rs, rowNum) -> {
    // null을 허용하는 getTimestamp("updated_at")에서 NPE 방지하는 코드
    java.sql.Timestamp updatedAtTs = rs.getTimestamp("updated_at");
    LocalDateTime updatedAt = null;

    if (updatedAtTs != null) {
      updatedAt = updatedAtTs.toLocalDateTime();
    }

    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

    return new Article(rs.getLong("id"),
        rs.getLong("author_id"),
        rs.getString("title"),
        rs.getString("content"),
        createdAt,
        updatedAt);
  };

  @Override
  public Article add(Article article) {
    String sql = "INSERT INTO article (title, content, author_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, article.getTitle());
      ps.setString(2, article.getContent());
      ps.setLong(3, article.getAuthorId());
      ps.setTimestamp(4, java.sql.Timestamp.valueOf(article.getCreatedAt()));
      ps.setTimestamp(5, java.sql.Timestamp.valueOf(article.getCreatedAt()));
      return ps;
    }, keyHolder);

    Long articleId = keyHolder.getKey().longValue();
    article.setId(articleId);

    return article;
  }

  @Override
  public Article update(Long articleId, Article article) {
    String sql = "UPDATE article SET title = ?, content = ?, updated_at = ? WHERE id = ?";

    int updatedRows = jdbcTemplate.update(
        sql,
        article.getTitle(),
        article.getContent(),
        LocalDateTime.now(),
        articleId
    );

    if (updatedRows == 0) {
      throw new ArticleNotFoundException(articleId);
    }

    return findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException("업데이트 후 조회 실패"));
  }

  @Override
  public void delete(Long articleId) {
    String sql = "DELETE FROM article WHERE id = ?";

    int updatedRows = jdbcTemplate.update(sql, articleId);

    if (updatedRows == 0) {
      throw new ArticleNotFoundException(articleId);
    }
  }

  @Override
  public List<Article> findAll() {
    String sql = "SELECT id, author_id, title, content, created_at, updated_at FROM article";

    return jdbcTemplate.query(sql, articleRowMapper);
  }

  @Override
  public List<Article> findBySearchKeyword(String search) {
    return List.of();
  }

  public Optional<Article> findById(Long articleId) {
    String sql = "SELECT id, title, content, author_id, created_at, updated_at FROM article WHERE id = ?";

    List<Article> articles = jdbcTemplate.query(sql, articleRowMapper, articleId);

    if (articles.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(articles.get(0));
  }
}
