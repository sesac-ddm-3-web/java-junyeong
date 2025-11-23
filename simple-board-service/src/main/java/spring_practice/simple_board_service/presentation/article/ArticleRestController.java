package spring_practice.simple_board_service.presentation.article;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleRestController {

  private final ArticleService articleService;

  // 게시글 전체 조회 (검색어 조회 가능)
  @RequestMapping(value = "/articles", method = RequestMethod.GET)
  public ResponseEntity<ArticleResponses> getArticles() {
    return ResponseEntity.ok();
  }

  // id에 해당하는 게시글 조회
  @RequestMapping(value = "/articles/{articleId}", method = RequestMethod.GET)
  public ResponseEntity<ArticleResponses> getArticleById() {
    return ResponseEntity.ok();
  }

  // 게시글 생성
  @RequestMapping(value = "/articles", method = RequestMethod.POST)
  public ResponseEntity<ArticleResponses> createArticle() {
    return ResponseEntity.ok();
  }

  // 게시글 수정
  @RequestMapping(value = "/articles/{articleId}", method = RequestMethod.PATCH)
  public ResponseEntity<ArticleResponses> updateArticle() {
    return ResponseEntity.ok();
  }

  // 게시글 삭제
  @RequestMapping(value = "/articles/{articleId}", method = RequestMethod.DELETE)
  public ResponseEntity<ArticleResponses> deleteArticle() {
    return ResponseEntity.ok();
  }


}
