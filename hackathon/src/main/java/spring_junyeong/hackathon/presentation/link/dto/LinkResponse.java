package spring_junyeong.hackathon.presentation.link.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spring_junyeong.hackathon.domain.Link;

@Getter
@AllArgsConstructor
public class LinkResponse {
    Long id;
    boolean isFavorite;
    String url;
    String title;
    String imageSource;
    String description;
    LocalDateTime createdAt;

    public LinkResponse(Link link) {
        this.id = link.getId();
        this.isFavorite = link.isFavorite();
        this.url = link.getUrl();
        this.title = link.getTitle();
        this.imageSource = link.getImageSource();
        this.description = link.getDescription();
        this.createdAt = link.getCreatedAt();
    }
}
