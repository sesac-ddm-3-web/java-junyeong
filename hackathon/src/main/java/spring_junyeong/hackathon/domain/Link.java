package spring_junyeong.hackathon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "link")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Link {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private boolean favorite;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false, length = 255)
  private String title;

  private String imageSource;

  private String description;

  @Column(updatable = false)
  private LocalDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "folder_id", nullable = false)
  @Setter(AccessLevel.PACKAGE) // Folder 엔티티에서만 관계 설정 가능하도록 Setter 제한
  private Folder folder;

  @Builder
  public Link(String url, String title, String imageSource, String description, Folder folder) {
    this.favorite = false;
    this.url = url;
    this.title = title;
    this.imageSource = imageSource;
    this.description = description;
    this.createdAt = LocalDateTime.now();
    this.folder = folder;
  }

  public void updateUrl(String url) {
    this.url = url;
  }

  public void toggleFavorite() {
    this.favorite = !this.favorite;
  }

  public boolean isKeywordExist(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return false;
    }
    String lowerKeyword = keyword.toLowerCase();

    // Null 체크 후 소문자로 변환하여 비교
    boolean titleMatch = Objects.nonNull(title) && title.toLowerCase().contains(lowerKeyword);
    boolean descriptionMatch =
        Objects.nonNull(description) && description.toLowerCase().contains(lowerKeyword);

    return titleMatch || descriptionMatch;
  }
}