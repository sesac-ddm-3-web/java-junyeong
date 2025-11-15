package spring_junyeong.hackathon.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Link {
    private Long id;
    private boolean favorite;
    private String url;
    private String title;
    private String imageSource;
    private String description;
    private LocalDateTime createdAt;

    public void setId(Long id) {
        this.id = id;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

    public void toggleFavorite() {
        this.favorite = !this.favorite;
    }

    public Link(String url, String title, String imageSource, String description) {
        this.favorite = false;
        this.url = url;
        this.title = title;
        this.imageSource = imageSource;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

}
