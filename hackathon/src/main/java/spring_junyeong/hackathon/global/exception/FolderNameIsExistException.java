package spring_junyeong.hackathon.global.exception;

public class FolderNameIsExistException extends RuntimeException {
    
    private static final int STATUS_CODE = 400; 

    public FolderNameIsExistException() {
        super("이미 존재하는 폴더 이름입니다."); 
    }
    
    public FolderNameIsExistException(String message) {
        super(message);
    }
    
    public int getStatusCode() {
        return STATUS_CODE;
    }

}