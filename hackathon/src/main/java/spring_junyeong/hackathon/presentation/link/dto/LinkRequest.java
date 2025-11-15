package spring_junyeong.hackathon.presentation.link.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LinkRequest {
    @NotBlank
    String url;

    @NotNull
    Long folderId;

    // public Link toEntity(LinkCreateResponse dto) {
    //     return new Link(url, title, description, createdAt, isFavorite);
    // }

    // public LinkCreateRequest toDto(Link entity) {
    //     return new LinkCreateRequest(entity.getTitle(), entity.getDescription(), entity.getCreatedAt(), entity.getIsFavorite(),)
    // }
}
