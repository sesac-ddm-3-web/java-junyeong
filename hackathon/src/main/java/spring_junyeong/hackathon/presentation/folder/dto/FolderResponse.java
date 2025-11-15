package spring_junyeong.hackathon.presentation.folder.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spring_junyeong.hackathon.domain.Folder;

@Getter
@AllArgsConstructor
public class FolderResponse {
    private Long id;
    private String name;
    private LocalDateTime createdAt;   
     
    public FolderResponse(Folder folder) {
        this.id = folder.getId();
        this.name = folder.getName();
        this.createdAt = folder.getCreatedAt();
    }
}
