package spring_junyeong.hackathon.presentation.link.dto;

import lombok.Getter;

@Getter
public class LinkMetadata {
    private String title;
    private String description;
    private String imageSource;

    public LinkMetadata(String title, String description, String imageSource) {
        this.title = title;
        this.description = description;
        this.imageSource = imageSource;
    }

}
