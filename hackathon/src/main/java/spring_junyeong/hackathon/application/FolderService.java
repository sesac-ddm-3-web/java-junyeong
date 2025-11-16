package spring_junyeong.hackathon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring_junyeong.hackathon.domain.Folder;
import spring_junyeong.hackathon.global.exception.FolderNameIsExistException;
import spring_junyeong.hackathon.global.exception.FolderNotFoundException;
import spring_junyeong.hackathon.infrastructure.FolderRepository;
import spring_junyeong.hackathon.presentation.folder.dto.FolderRequest;
import spring_junyeong.hackathon.presentation.folder.dto.FolderResponse;

@Service
@RequiredArgsConstructor
public class FolderService {

  private final FolderRepository folderRepository;

  public FolderResponse createFolder(FolderRequest request) {
    validateFolderName(request.getName());

    Folder folder = folderRepository.add(new Folder(request.getName()));

    return new FolderResponse(folder);
  }

  public FolderResponse getFolderInfo(Long id) {
    Folder folder = folderRepository.findById(id).orElseThrow();

    return new FolderResponse(folder);
  }

  public List<FolderResponse> getFolders() {
    return folderRepository.findAll().stream()
        .map(FolderResponse::new)
        .toList();
  }

  public void deleteFolder(Long folderId) {
    folderRepository.delete(folderId);
  }

  public FolderResponse updateFolderName(Long folderId, String name) {
    Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new FolderNotFoundException(folderId));

    folder.rename(name);

    return new FolderResponse(folder);
  }

  private void validateFolderName(String name) {
    folderRepository.findByName(name)
        .orElseThrow(() -> new FolderNameIsExistException("이미 존재하는 폴더 이름입니다."));
    }

}

