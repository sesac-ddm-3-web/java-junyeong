package spring_practice.simple_board_service.presentation.article;

import jakarta.validation.Valid;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring_practice.simple_board_service.application.article.ArticleService;
import spring_practice.simple_board_service.application.auth.TokenService;

@RestController
@RequiredArgsConstructor
public class ArticleRestController {

  private final ArticleService articleService;
  private final TokenService tokenService;

  // 게시글 전체 조회 (검색어 조회 가능)
  @RequestMapping(value = "/articles", method = RequestMethod.GET)
  public ResponseEntity<ArticlesResponse> getArticles(
      @RequestParam(value = "search", required = false) String search) {
    ArticlesResponse response = articleService.getArticles(search);

    return ResponseEntity.ok(response);
  }

  // id에 해당하는 게시글 조회
  @RequestMapping(value = "/articles/{articleId}", method = RequestMethod.GET)
  public ResponseEntity<ArticleResponse> getArticleById(
      @PathVariable("articleId") Long articleId) {
    ArticleResponse response = articleService.getArticleById(articleId);

    return ResponseEntity.ok(response);
  }

  // 게시글 생성
  @RequestMapping(value = "/articles", method = RequestMethod.POST)
  public ResponseEntity<ArticleResponse> createArticle(
      @Valid @RequestBody ArticleCreateRequest articleCreateRequest) {
    ArticleResponse response = articleService.createArticle(articleCreateRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 게시글 수정
  @RequestMapping(value = "/articles/{articleId}", method = RequestMethod.PATCH)
  public ResponseEntity<ArticleResponse> updateArticle(
      @Valid @RequestBody ArticleUpdateRequest articleUpdateRequest) {
    ArticleResponse response = articleService.updateArticle(articleUpdateRequest);

    return ResponseEntity.ok(response);
  }

  // 게시글 삭제
  @RequestMapping(value = "/articles/{articleId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteArticle(@PathVariable("articleId") Long articleId)
      throws AccessDeniedException {

    articleService.deleteArticle(articleId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }


}
