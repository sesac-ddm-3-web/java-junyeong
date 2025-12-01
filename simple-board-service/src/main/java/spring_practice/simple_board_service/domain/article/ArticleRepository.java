package spring_practice.simple_board_service.domain.article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

  public Article add(Article article);

  public Article update(Long articleId, Article article);

  public void delete(Long articleId);

  public List<Article> findAll();

  public List<Article> findBySearchKeyword(String search);

  public Optional<Article> findById(Long articleId);

}
