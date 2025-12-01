package spring_practice.simple_board_service.presentation.article;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring_practice.simple_board_service.application.article.ArticleService;
import spring_practice.simple_board_service.application.auth.TokenService;
import spring_practice.simple_board_service.application.comment.CommentService;
import spring_practice.simple_board_service.presentation.article.dto.ArticleCreateRequest;
import spring_practice.simple_board_service.presentation.article.dto.ArticleResponse;
import spring_practice.simple_board_service.presentation.article.dto.ArticleUpdateRequest;
import spring_practice.simple_board_service.presentation.article.dto.ArticlesResponse;
import spring_practice.simple_board_service.presentation.comments.dto.CommentCreateRequest;
import spring_practice.simple_board_service.presentation.comments.dto.CommentResponse;
import spring_practice.simple_board_service.presentation.comments.dto.CommentResponses;
import spring_practice.simple_board_service.presentation.comments.dto.CommentUpdateRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleRestController {

  private final ArticleService articleService;
  private final CommentService commentService;
  private final TokenService tokenService;

  // 게시글 전체 조회 (검색어 조회 가능)
  @GetMapping
  public ResponseEntity<ArticlesResponse> getArticles(
      @RequestParam(required = false) String search) {
    ArticlesResponse response = articleService.getArticles(search);

    return ResponseEntity.ok(response);
  }

  // id에 해당하는 게시글 조회
  @GetMapping("/{articleId}")
  public ResponseEntity<ArticleResponse> getArticleById(
      @PathVariable Long articleId) {
    ArticleResponse response = articleService.getArticleById(articleId);

    return ResponseEntity.ok(response);
  }

  // 게시글 생성
  @PostMapping
  public ResponseEntity<ArticleResponse> createArticle(
      @Valid @RequestBody ArticleCreateRequest articleCreateRequest) {
    ArticleResponse response = articleService.createArticle(articleCreateRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 게시글 수정
  @PatchMapping("/{articleId}")
  public ResponseEntity<ArticleResponse> updateArticle(
      @Valid @RequestBody ArticleUpdateRequest articleUpdateRequest,
      @PathVariable Long articleId) {
    ArticleResponse response = articleService.updateArticle(articleId, articleUpdateRequest);

    return ResponseEntity.ok(response);
  }

  // 게시글 삭제
  @DeleteMapping("/{articleId}")
  public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {

    articleService.deleteArticle(articleId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  // 모든 댓글 조회
  @GetMapping("/{articleId}/comments")
  public ResponseEntity<CommentResponses> getCommentsByArticleId(@PathVariable Long articleId) {
    CommentResponses responses = commentService.findAllByArticleId(articleId);

    return ResponseEntity.ok(responses);
  }

  // 댓글 생성
  @PostMapping("/{articleId}/comments")
  public ResponseEntity<CommentResponse> addComment(
      @PathVariable Long articleId, @Valid @RequestBody CommentCreateRequest request) {
    CommentResponse response = commentService.add(articleId, request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 댓글 수정
  @PatchMapping("/{articleId}/comments/{commentId}")
  public ResponseEntity<CommentResponse> updateComment(
      @PathVariable Long commentId, @Valid @RequestBody CommentUpdateRequest request) {

    CommentResponse response = commentService.update(commentId, request);

    return ResponseEntity.ok(response);
  }

  // 댓글 삭제
  @DeleteMapping("/{articleId}/comments/{commentId}")
  public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
    commentService.delete(commentId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
