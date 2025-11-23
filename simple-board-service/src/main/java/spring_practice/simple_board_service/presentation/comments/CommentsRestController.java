package spring_practice.simple_board_service.presentation.comments;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentsRestController {
  private final CommentsService coomentsService;

  // 댓글 전체 조회
  @RequestMapping(value = "/comments", method = RequestMethod.GET)
  public ResponseEntity<List<CommentsResponse>> getComments() {
    return ResponseEntity.ok();
  }

  // 댓글 생성
  @RequestMapping(value = "/comments", method = RequestMethod.POST)
  public ResponseEntity<CommentsResponse> getComments() {
    return ResponseEntity.ok();
  }

  // 댓글 수정
  @RequestMapping(value = "/comments/{commentsId}", method = RequestMethod.PATCH)
  public ResponseEntity<CommentsResponse> getComments() {
    return ResponseEntity.ok();
  }

  // 댓글 삭제
  @RequestMapping(value = "/comments/{commentsId}", method = RequestMethod.DELETE)
  public ResponseEntity<CommentsDeleteResponse> getComments() {
    return ResponseEntity.ok();
  }

}
