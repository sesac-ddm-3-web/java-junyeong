package spring_junyeong.hackathon.presentation.folder.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring_junyeong.hackathon.application.FolderService;
import spring_junyeong.hackathon.presentation.folder.dto.FolderRequest;
import spring_junyeong.hackathon.presentation.folder.dto.FolderResponse;

@RestController
@RequiredArgsConstructor
public class FolderRestController {

  private final FolderService folderService;

  // 새 폴더 생성
  @RequestMapping(value = "/folders", method = RequestMethod.POST)
  public ResponseEntity<FolderResponse> createFolders(@RequestBody FolderRequest folderRequest) {
    FolderResponse folderCreateResponse = folderService.createFolder(folderRequest);

    return ResponseEntity.status(HttpStatus.CREATED).body(folderCreateResponse);
  }
  
  // 전체 폴더 목록 조회
  @RequestMapping(value = "/folders", method = RequestMethod.GET)
  public ResponseEntity<List<FolderResponse>> getFolders() {
    List<FolderResponse> response = folderService.getFolders();

    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "/folders/{forderId}", method = RequestMethod.PUT)
  public ResponseEntity<FolderResponse> updateFolder(@PathVariable Long folderId,
      @RequestBody FolderRequest request) {
    FolderResponse response = folderService.updateFolderName(folderId, request.getName());

    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "/folders/{forderId}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> removeFolder(@PathVariable Long folderId) {
    folderService.deleteFolder(folderId);

    return ResponseEntity.noContent().build();
  }

}
