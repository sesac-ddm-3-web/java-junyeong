package spring_junyeong.hackathon.presentation.link.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.LinkService;
import spring_junyeong.hackathon.presentation.link.dto.LinkRequest;
import spring_junyeong.hackathon.presentation.link.dto.LinkResponse;
import spring_junyeong.hackathon.presentation.link.dto.LinkUpdateRequest;
import spring_junyeong.hackathon.presentation.link.dto.LinksResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/links")
public class LinkRestController {

  private final LinkService linkService;

  @PostMapping
  public ResponseEntity<LinkResponse> createLink(@Valid @RequestBody LinkRequest request) {
    LinkResponse response = linkService.createLink(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public ResponseEntity<LinksResponse> getAllLinks(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String search
  ) {
    LinksResponse response = linkService.getAllLinks(page, pageSize, search);
    return ResponseEntity.ok().body(response);
  }

  // 즐겨찾기 링크 목록 조회
  @GetMapping("/favorites")
  public ResponseEntity<List<LinkResponse>> getFavorites() {
    List<LinkResponse> response = linkService.getFavoriteLinks();

    return ResponseEntity.ok().body(response);
  }

  // 링크 URL 수정
  @PutMapping("/{linkId}")
  public ResponseEntity<LinkResponse> updateLink(@PathVariable Long linkId,
      @RequestBody LinkUpdateRequest request) {
    LinkResponse response = linkService.updateLink(linkId, request.getUrl());

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 링크 즐겨찾기 토글
  @PutMapping("/{linkId}/favorites")
  public ResponseEntity<LinkResponse> toggleLinkFavorites(@PathVariable Long linkId) {
    LinkResponse response = linkService.toggleLinkFavorite(linkId);

    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  // 링크 삭제
  @DeleteMapping("/{linkdId}")
  public ResponseEntity<Void> removeLink(@PathVariable Long linkId) {
    linkService.removeLink(linkId);

    return ResponseEntity.noContent().build();
  }

}
