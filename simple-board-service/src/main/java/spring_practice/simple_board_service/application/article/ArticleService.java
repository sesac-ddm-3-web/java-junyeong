package spring_practice.simple_board_service.application.article;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.common.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.common.exception.AuthorizationException;
import spring_practice.simple_board_service.domain.article.Article;
import spring_practice.simple_board_service.domain.article.ArticleRepository;
import spring_practice.simple_board_service.domain.auth.AuthenticationContext;
import spring_practice.simple_board_service.domain.auth.UserRepository;
import spring_practice.simple_board_service.presentation.article.dto.ArticleCreateRequest;
import spring_practice.simple_board_service.presentation.article.dto.ArticleResponse;
import spring_practice.simple_board_service.presentation.article.dto.ArticleUpdateRequest;
import spring_practice.simple_board_service.presentation.article.dto.ArticlesResponse;

@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;

  // 게시글 전체 조회 (검색어 조회 가능)
  public ArticlesResponse getArticles(String search) {
    List<Article> articles;

    if (search == null) {
      articles = articleRepository.findAll();
    } else {
      articles = articleRepository.findBySearchKeyword(search);
    }

    return new ArticlesResponse(articles.stream().map(ArticleResponse::new).toList());

  }

  // id에 해당하는 게시글 조회
  public ArticleResponse getArticleById(Long articleId) {
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

    return new ArticleResponse(article);
  }

  // 게시글 생성
  public ArticleResponse createArticle(ArticleCreateRequest request) {
    Long currentUserId = AuthenticationContext.getPrincipal().getUserId();

    Article article = request.toEntity();
    article.setAuthorId(currentUserId);

    article = articleRepository.add(article);

    return new ArticleResponse(article);

  }

  // 게시글 수정
  public ArticleResponse updateArticle(Long updateArticleId, ArticleUpdateRequest request) {

    Long currentUserId = AuthenticationContext.getPrincipal().getUserId();

    Article article = articleRepository.findById(updateArticleId)
        .orElseThrow(() -> new ArticleNotFoundException(updateArticleId));

    if (!article.isAuthorEqual(currentUserId)) {
      throw new AuthorizationException("게시글을 수정할 권한이 없습니다.");
    }

    article.update(request.getTitle(), request.getContent());

    article = articleRepository.update(updateArticleId, article);

    return new ArticleResponse(article);

  }

  // 게시글 삭제
  public void deleteArticle(Long articleId) {

    Long currentUserId = AuthenticationContext.getPrincipal().getUserId();

    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

    Boolean isAuthorEqual = article.isAuthorEqual(currentUserId);

    if (isAuthorEqual) {
      articleRepository.delete(articleId);
    } else {
      throw new AuthorizationException("게시글을 지울 권한이 없습니다.");
    }
  }

}
