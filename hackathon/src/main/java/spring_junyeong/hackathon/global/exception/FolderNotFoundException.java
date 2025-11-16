package spring_junyeong.hackathon.global.exception;

public class FolderNotFoundException extends RuntimeException {

  // 생성자에서 찾을 수 없는 ID를 받아서 메시지를 만듭니다.
  public FolderNotFoundException(Long folderId) {
    super("폴더 ID [" + folderId + "]에 해당하는 폴더를 찾을 수 없습니다.");
  }
  
}
