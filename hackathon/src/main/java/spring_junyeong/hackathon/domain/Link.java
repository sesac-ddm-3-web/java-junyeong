package spring_junyeong.hackathon.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Link {

  @Setter
  private Long id;
  private boolean favorite;
  private String url;
  private String title;
  private String imageSource;
  private String description;
  private LocalDateTime createdAt;


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

  public boolean isKeywordExist(String keyword) {
    return title.contains(keyword) || description.contains(keyword);
  }

}
