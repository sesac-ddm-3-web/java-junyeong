package spring_junyeong.hackathon.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_junyeong.hackathon.domain.Folder;
import spring_junyeong.hackathon.domain.FolderRepository;
import spring_junyeong.hackathon.global.exception.FolderNameIsExistException;
import spring_junyeong.hackathon.global.exception.FolderNotFoundException;
import spring_junyeong.hackathon.presentation.folder.dto.FolderRequest;
import spring_junyeong.hackathon.presentation.folder.dto.FolderResponse;

@Service
@RequiredArgsConstructor
public class FolderService {

  private final FolderRepository folderRepository;

  @Transactional
  public FolderResponse createFolder(FolderRequest request) {
    if (folderRepository.existsByName(request.getName())) {
      throw new FolderNameIsExistException("이미 존재하는 폴더 이름입니다.");
    }

    Folder folder = new Folder(request.getName());
    folder = folderRepository.save(folder);

    return new FolderResponse(folder);
  }

  @Transactional(readOnly = true)
  public FolderResponse getFolderInfo(Long id) {
    Folder folder = folderRepository.findById(id)
        .orElseThrow(() -> new FolderNotFoundException(id));

    return new FolderResponse(folder);
  }

  @Transactional(readOnly = true)
  public List<FolderResponse> getFolders() {
    return folderRepository.findAll().stream()
        .map(FolderResponse::new)
        .toList();
  }

  @Transactional
  public void deleteFolder(Long folderId) {
    folderRepository.deleteById(folderId);
  }

  @Transactional
  public FolderResponse updateFolderName(Long folderId, String name) {
    Folder folder = folderRepository.findById(folderId)
        .orElseThrow(() -> new FolderNotFoundException(folderId));

    folder.rename(name);

    return new FolderResponse(folder);
  }

}

