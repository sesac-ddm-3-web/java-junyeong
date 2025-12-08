package spring_junyeong.hackathon.presentation.folder.controller;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.FolderService;
import spring_junyeong.hackathon.application.LinkService;
import spring_junyeong.hackathon.presentation.folder.dto.FolderRequest;
import spring_junyeong.hackathon.presentation.folder.dto.FolderResponse;
import spring_junyeong.hackathon.presentation.link.dto.LinksResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/folders")
public class FolderRestController {

  private final FolderService folderService;
  private final LinkService linkService;
  
  @PostMapping
  public ResponseEntity<FolderResponse> createFolders(@RequestBody FolderRequest folderRequest) {
    FolderResponse folderCreateResponse = folderService.createFolder(folderRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(folderCreateResponse);
  }

  @GetMapping
  public ResponseEntity<List<FolderResponse>> getFolders() {
    List<FolderResponse> response = folderService.getFolders();

    return ResponseEntity.ok(response);
  }

  @PutMapping("/{folderId}")
  public ResponseEntity<FolderResponse> updateFolder(@PathVariable Long folderId,
      @RequestBody FolderRequest request) {
    FolderResponse response = folderService.updateFolderName(folderId, request.getName());

    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{folderId}")
  public ResponseEntity<Void> removeFolder(@PathVariable Long folderId) {
    folderService.deleteFolder(folderId);

    return ResponseEntity.noContent().build();
  }

  // 특정 폴더의 링크 목록 조회
  @RequestMapping(value = "/folders/{folderId}/links", method = RequestMethod.GET)
  public ResponseEntity<LinksResponse> getLinksInFolder(@PathVariable Long folderId) {
    LinksResponse response = linkService.getLinksInFolder(folderId);

    return ResponseEntity.ok().body(response);
  }

}
