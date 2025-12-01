package spring_practice.simple_board_service.application.comment;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_practice.simple_board_service.application.auth.TokenService;
import spring_practice.simple_board_service.common.exception.ArticleNotFoundException;
import spring_practice.simple_board_service.common.exception.AuthorizationException;
import spring_practice.simple_board_service.common.exception.CommentNotFoundException;
import spring_practice.simple_board_service.domain.article.Article;
import spring_practice.simple_board_service.domain.article.ArticleRepository;
import spring_practice.simple_board_service.domain.auth.AuthenticationContext;
import spring_practice.simple_board_service.domain.comment.Comment;
import spring_practice.simple_board_service.domain.comment.CommentRepository;
import spring_practice.simple_board_service.presentation.comments.dto.CommentCreateRequest;
import spring_practice.simple_board_service.presentation.comments.dto.CommentResponse;
import spring_practice.simple_board_service.presentation.comments.dto.CommentResponses;
import spring_practice.simple_board_service.presentation.comments.dto.CommentUpdateRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;
  private final TokenService tokenService;

  public CommentResponses findAllByArticleId(Long articleId) {
    validateArticleExistence(articleId);

    List<Comment> comments = commentRepository.findAllByArticleId(articleId);

    return new CommentResponses(comments.stream().map(CommentResponse::new).toList());
  }

  public CommentResponse findById(Long commentId) {
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new CommentNotFoundException());

    return new CommentResponse(comment);

  }

  public CommentResponse add(Long articleId, CommentCreateRequest request) {
    validateArticleExistence(articleId);

    Long currentUserId = AuthenticationContext.getPrincipal().getUserId();

    Comment comment = request.toEntity(articleId, currentUserId);

    Comment idSettedComment = commentRepository.add(comment);

    return new CommentResponse(idSettedComment);
  }

  public CommentResponse update(Long commentId, CommentUpdateRequest request) {

    Comment comment = validateCommentExistence(commentId);

    Long currentUserId = AuthenticationContext.getPrincipal().getUserId();

    if (!currentUserId.equals(comment.getAuthorId())) {
      throw new AuthorizationException("댓글을 수정할 권한이 없습니다.");
    } else {
      comment.updateText(request.getText());
      commentRepository.update(comment);
    }

    return new CommentResponse(comment);
  }

  public void delete(Long commentId) {

    Comment comment = validateCommentExistence(commentId);

    Long currentUserId = AuthenticationContext.getPrincipal().getUserId();

    if (!currentUserId.equals(comment.getAuthorId())) {
      throw new AuthorizationException("댓글을 삭제할 권한이 없습니다.");
    } else {
      commentRepository.delete(commentId);
    }
  }

  private Article validateArticleExistence(Long articleId) {
    return articleRepository.findById(articleId)
        .orElseThrow(() -> new ArticleNotFoundException(articleId));

  }

  private Comment validateCommentExistence(Long commentId) {
    return commentRepository.findById(commentId)
        .orElseThrow(() -> new CommentNotFoundException(commentId));
  }

}
